package com.framework.base;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.framework.config.ConfigReader;
import com.framework.driver.DriverManager;
import com.framework.utils.ExtentReportManager;
import com.framework.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;

/**
 * Base test class. All test classes must extend this.
 * Handles driver lifecycle and ExtentReports integration.
 */
public class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        new File("reports/screenshots").mkdirs();
        log.info("Test suite started.");
    }

    /**
     * Initializes WebDriver and ExtentTest before each test method.
     *
     * @param result ITestResult injected by TestNG
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        log.info("Starting test: {}", testName);

        DriverManager.initDriver();
        DriverManager.getDriver().get(ConfigReader.get("base.url"));

        ExtentReportManager.createTest(testName, description != null ? description : "");
        ExtentReportManager.logInfo("Browser launched → " + ConfigReader.get("base.url"));
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
     * Tears down driver after each test. Captures screenshot on failure.
     *
     * @param result ITestResult injected by TestNG
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.FAILURE) {
            try {
                String base64 = ScreenshotUtils.captureBase64(getDriver());
                ExtentReportManager.logFail("Test FAILED: " + result.getThrowable().getMessage());
                ExtentReportManager.getTest()
                        .fail("Screenshot on failure",
                                MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
            } catch (Exception e) {
                log.warn("Could not attach screenshot: {}", e.getMessage());
            }
            log.error("Test FAILED: {}", testName, result.getThrowable());

        } else if (result.getStatus() == ITestResult.SUCCESS) {
            ExtentReportManager.logPass("Test PASSED ✅");
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
        log.info("Report → reports/ExtentReport.html");
    }
}
