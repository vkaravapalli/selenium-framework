package com.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for capturing and saving screenshots.
 */
public class ScreenshotUtils {

    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "reports/screenshots/";

    private ScreenshotUtils() {}

    /**
     * Captures a screenshot and saves it to reports/screenshots/.
     *
     * @param driver   WebDriver instance
     * @param testName name used in filename
     * @return absolute path to the saved screenshot, or empty string on failure
     */
    public static String capture(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String fullPath = SCREENSHOT_DIR + fileName;

        try {
            new File(SCREENSHOT_DIR).mkdirs();
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path path = Paths.get(fullPath);
            Files.write(path, screenshot);
            log.info("Screenshot saved: {}", fullPath);
            return fullPath;
        } catch (IOException e) {
            log.error("Failed to save screenshot: {}", fullPath, e);
            return "";
        }
    }

    /**
     * Returns a Base64-encoded screenshot string (useful for embedding in reports).
     *
     * @param driver WebDriver instance
     * @return Base64 string
     */
    public static String captureBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }
}
