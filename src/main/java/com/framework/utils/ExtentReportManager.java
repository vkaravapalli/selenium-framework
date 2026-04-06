package com.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Singleton manager for ExtentReports.
 * Creates a Spark HTML report at reports/ExtentReport.html.
 */
public class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);
    private static final String REPORT_PATH = "reports/ExtentReport.html";

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    private ExtentReportManager() {}

    /**
     * Returns the singleton ExtentReports instance, creating it if needed.
     *
     * @return ExtentReports instance
     */
    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setReportName("Selenium Test Results");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setEncoding("UTF-8");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Author", "Automation Framework");

            log.info("ExtentReports initialized. Report path: {}", REPORT_PATH);
        }
        return extent;
    }

    /**
     * Creates a new test entry in the report for the current thread.
     *
     * @param testName name of the test
     * @return ExtentTest instance
     */
    public static ExtentTest createTest(String testName) {
        ExtentTest test = getInstance().createTest(testName);
        testThread.set(test);
        return test;
    }

    /**
     * Creates a new test entry with description.
     *
     * @param testName    name of the test
     * @param description test description
     * @return ExtentTest instance
     */
    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        testThread.set(test);
        return test;
    }

    /**
     * Returns the ExtentTest for the current thread.
     *
     * @return ExtentTest instance
     */
    public static ExtentTest getTest() {
        return testThread.get();
    }

    /**
     * Logs a PASS step to the current test.
     *
     * @param message log message
     */
    public static void logPass(String message) {
        getTest().pass(message);
    }

    /**
     * Logs a FAIL step to the current test.
     *
     * @param message log message
     */
    public static void logFail(String message) {
        getTest().fail(message);
    }

    /**
     * Logs an INFO step to the current test.
     *
     * @param message log message
     */
    public static void logInfo(String message) {
        getTest().info(message);
    }

    /**
     * Flushes and writes the report to disk. Call once after all tests.
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            log.info("ExtentReports flushed to: {}", REPORT_PATH);
        }
    }
}
