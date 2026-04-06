package com.framework.listeners;

import com.framework.utils.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener for logging test lifecycle events to ExtentReports.
 * Register in testng.xml: <listeners><listener class-name="com.framework.listeners.TestListener"/></listeners>
 */
public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        log.info("▶ Starting: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✅ PASSED: {}", result.getMethod().getMethodName());
        ExtentReportManager.logPass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("❌ FAILED: {}", result.getMethod().getMethodName());
        ExtentReportManager.logFail("Test failed: " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⏭ SKIPPED: {}", result.getMethod().getMethodName());
        ExtentReportManager.logInfo("Test skipped");
    }

    @Override
    public void onStart(ITestContext context) {
        log.info("🚀 Suite started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("🏁 Suite finished: {} | Passed: {} | Failed: {} | Skipped: {}",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        ExtentReportManager.flushReports();
    }
}
