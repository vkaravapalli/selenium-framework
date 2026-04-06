package com.framework.base;

import com.framework.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * Base class for all Page Objects.
 * Provides common interactions and wait utilities.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WaitUtils wait;
    private static final Logger log = LogManager.getLogger(BasePage.class);

    /**
     * Initializes the page with the given driver and sets up PageFactory.
     *
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks the element identified by the locator.
     *
     * @param locator By locator
     */
    protected void click(By locator) {
        log.debug("Clicking element: {}", locator);
        wait.waitForClickability(locator).click();
    }

    /**
     * Clicks a WebElement after waiting for it to be clickable.
     *
     * @param element WebElement to click
     */
    protected void click(WebElement element) {
        log.debug("Clicking WebElement");
        wait.waitForClickability(element).click();
    }

    /**
     * Clears the field and types the given text.
     *
     * @param locator By locator
     * @param text    text to type
     */
    protected void sendKeys(By locator, String text) {
        log.debug("Typing '{}' into: {}", text, locator);
        WebElement element = wait.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Clears the field and types the given text into a WebElement.
     *
     * @param element WebElement
     * @param text    text to type
     */
    protected void sendKeys(WebElement element, String text) {
        log.debug("Typing '{}' into WebElement", text);
        wait.waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Returns the visible text of the element.
     *
     * @param locator By locator
     * @return text content
     */
    protected String getText(By locator) {
        return wait.waitForVisibility(locator).getText();
    }

    /**
     * Returns the visible text of the WebElement.
     *
     * @param element WebElement
     * @return text content
     */
    protected String getText(WebElement element) {
        return wait.waitForVisibility(element).getText();
    }

    /**
     * Returns true if the element is displayed.
     *
     * @param locator By locator
     * @return true if displayed
     */
    protected boolean isDisplayed(By locator) {
        try {
            return wait.waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the WebElement is displayed.
     *
     * @param element WebElement
     * @return true if displayed
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            return wait.waitForVisibility(element).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits for the element to be visible and returns it.
     *
     * @param locator By locator
     * @return visible WebElement
     */
    protected WebElement waitForElement(By locator) {
        return wait.waitForVisibility(locator);
    }

    /**
     * Returns the current page title.
     *
     * @return page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Returns the current page URL.
     *
     * @return current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
