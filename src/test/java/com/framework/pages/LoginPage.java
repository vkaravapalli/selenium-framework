package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for the SauceDemo Login page.
 * URL: https://www.saucedemo.com
 */
public class LoginPage extends BasePage {

    // --- Locators via @FindBy (PageFactory) ---

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    @FindBy(className = "inventory_list")
    private WebElement inventoryList;

    /**
     * Initializes LoginPage with the given driver.
     *
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Types into the username field.
     *
     * @param username username string
     */
    public void enterUsername(String username) {
        sendKeys(usernameField, username);
    }

    /**
     * Types into the password field.
     *
     * @param password password string
     */
    public void enterPassword(String password) {
        sendKeys(passwordField, password);
    }

    /**
     * Clicks the Login button.
     */
    public void clickLogin() {
        click(loginButton);
    }

    /**
     * Performs a full login action.
     *
     * @param username username
     * @param password password
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    /**
     * Returns the text of the error message element.
     *
     * @return error message text
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Returns true if the error message is displayed.
     *
     * @return true if error is shown
     */
    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    /**
     * Returns true if the login was successful (inventory list visible).
     *
     * @return true if logged in
     */
    public boolean isLoginSuccessful() {
        return isDisplayed(inventoryList);
    }

    /**
     * Returns true if we are on the login page.
     *
     * @return true if login button is visible
     */
    public boolean isOnLoginPage() {
        return isDisplayed(loginButton);
    }
}
