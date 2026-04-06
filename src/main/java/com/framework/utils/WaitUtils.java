package com.framework.utils;

import com.framework.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Wrapper around WebDriverWait providing common wait conditions.
 */
public class WaitUtils {

    private static final Logger log = LogManager.getLogger(WaitUtils.class);
    private final WebDriverWait wait;

    /**
     * Creates WaitUtils with the explicit wait timeout from config.
     *
     * @param driver WebDriver instance
     */
    public WaitUtils(WebDriver driver) {
        int timeout = ConfigReader.getInt("explicit.wait");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    /**
     * Creates WaitUtils with a custom timeout in seconds.
     *
     * @param driver         WebDriver instance
     * @param timeoutSeconds custom timeout
     */
    public WaitUtils(WebDriver driver, int timeoutSeconds) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * Waits until the element is visible.
     *
     * @param locator By locator
     * @return visible WebElement
     */
    public WebElement waitForVisibility(By locator) {
        log.debug("Waiting for visibility of: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the WebElement is visible.
     *
     * @param element WebElement
     * @return visible WebElement
     */
    public WebElement waitForVisibility(WebElement element) {
        log.debug("Waiting for visibility of element");
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits until the element is clickable.
     *
     * @param locator By locator
     * @return clickable WebElement
     */
    public WebElement waitForClickability(By locator) {
        log.debug("Waiting for clickability of: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits until the element is clickable.
     *
     * @param element WebElement
     * @return clickable WebElement
     */
    public WebElement waitForClickability(WebElement element) {
        log.debug("Waiting for clickability of element");
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits until the element is invisible.
     *
     * @param locator By locator
     * @return true when invisible
     */
    public boolean waitForInvisibility(By locator) {
        log.debug("Waiting for invisibility of: {}", locator);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits until the page title contains the given text.
     *
     * @param titleFragment partial title text
     * @return true when title matches
     */
    public boolean waitForTitleContains(String titleFragment) {
        return wait.until(ExpectedConditions.titleContains(titleFragment));
    }

    /**
     * Waits until the URL contains the given fragment.
     *
     * @param urlFragment partial URL text
     * @return true when URL matches
     */
    public boolean waitForUrlContains(String urlFragment) {
        return wait.until(ExpectedConditions.urlContains(urlFragment));
    }
}
