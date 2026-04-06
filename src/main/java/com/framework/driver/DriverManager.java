package com.framework.driver;

import com.framework.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Manages WebDriver instances using ThreadLocal for safe parallel execution.
 * Browser is determined by system property "browser" or config.properties.
 */
public class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    private DriverManager() {}

    /**
     * Initializes a new WebDriver for the current thread.
     * Browser priority: system property > config.properties > chrome (default).
     */
    public static void initDriver() {
        String browser = System.getProperty("browser",
                ConfigReader.get("browser", "chrome")).toLowerCase();

        log.info("Initializing browser: {}", browser);

        WebDriver driver;
        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOptions = new FirefoxOptions();
                driver = new FirefoxDriver(ffOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                driver = new EdgeDriver(edgeOptions);
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        int implicitWait = ConfigReader.getInt("implicit.wait");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().window().maximize();

        driverThread.set(driver);
        log.info("Driver initialized for thread: {}", Thread.currentThread().getName());
    }

    /**
     * Returns the WebDriver for the current thread.
     *
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            throw new IllegalStateException("Driver not initialized. Call initDriver() first.");
        }
        return driverThread.get();
    }

    /**
     * Quits the WebDriver for the current thread and removes it from ThreadLocal.
     */
    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
            log.info("Driver quit for thread: {}", Thread.currentThread().getName());
        }
    }
}
