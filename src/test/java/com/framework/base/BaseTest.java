package com.framework.base;

import com.framework.config.ConfigReader;
import com.framework.driver.DriverManager;
import com.framework.utils.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Base test class. All test classes must extend this.
 * Handles driver lifecycle and ExtentReports integration.
 */
public class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        // Ensure reports directory exists
        new File("reports").mkdirs();
        new File("reports/screenshots").mkdirs();
        log.info("Test suite started.");
    }

    /**
     * Initializes the WebDriver and ExtentTest before each test method.
     *
     * @param result ITestResult injected by TestNG
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.info("Starting test: {}", testName);

        DriverManager.initDriver();

        String baseUrl = ConfigReader.get("base.url");
        DriverManager.getDriver().get(baseUrl);

        ExtentReportManager.createTest(testName,
                result.getMethod().getDescription());
        ExtentReportManager.logInfo("Browser launched. Navigated to: " + baseUrl);
    }

    /**
     * Returns the current thread's WebDriver.
     *
     * @return WebDriver instance
     */
    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    /**
     * Tears down the driver after each test.
     * Captures screenshot on failure and logs to ExtentReports.
     *
     * @param result ITestResult injected by TestNG
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = captureScreenshot(testName);
            ExtentReportManager.logFail("Test FAILED: " + result.getThrowable().getMessage());
            ExtentReportManager.getTest()
                    .addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
            log.error("Test FAILED: {}", testName, result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            ExtentReportManager.logPass("Test PASSED");
            log.info("Test PASSED: {}", testName);
        } else {
            ExtentReportManager.logInfo("Test SKIPPED");
            log.warn("Test SKIPPED: {}", testName);
        }

        DriverManager.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        ExtentReportManager.flushReports();
        log.info("Test suite finished. Report generated at: reports/ExtentReport.html");
    }

    /**
     * Captures a screenshot and saves it to reports/screenshots/.
     *
     * @param testName test name used in filename
     * @return absolute path to the screenshot file
     */
    private String captureScreenshot(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String screenshotDir = "reports/screenshots/";
        String fullPath = screenshotDir + fileName;

        try {
            byte[] screenshot = ((TakesScreenshot) getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            Path path = Paths.get(fullPath);
            Files.createDirectories(path.getParent());
            Files.write(path, screenshot);
            log.info("Screenshot saved: {}", fullPath);
        } catch (IOException e) {
            log.error("Failed to save screenshot", e);
        }

        return fullPath;
    }
}
