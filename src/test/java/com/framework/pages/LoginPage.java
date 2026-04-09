package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for https://practicetestautomation.com/practice-test-login/
 */
public class LoginPage extends BasePage {

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "submit")
    private WebElement submitButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    /**
     * Initializes LoginPage with the given driver.
     *
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Types into the Username field.
     *
     * @param username username string
     */
    public void enterUsername(String username) {
        sendKeys(usernameField, username);
    }

    /**
     * Types into the Password field.
     *
     * @param password password string
     */
    public void enterPassword(String password) {
        sendKeys(passwordField, password);
    }

    /**
     * Clicks the Submit button.
     */
    public void clickSubmit() {
        click(submitButton);
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
        clickSubmit();
    }

    /**
     * Returns the error message text.
     *
     * @return error message string
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Returns true if the error message is visible.
     *
     * @return true if error is displayed
     */
    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    /**
     * Returns true if the username field is visible.
     *
     * @return true if displayed
     */
    public boolean isUsernameFieldDisplayed() {
        return isDisplayed(usernameField);
    }

    /**
     * Returns true if the password field is visible.
     *
     * @return true if displayed
     */
    public boolean isPasswordFieldDisplayed() {
        return isDisplayed(passwordField);
    }

    /**
     * Returns true if the submit button is visible.
     *
     * @return true if displayed
     */
    public boolean isSubmitButtonDisplayed() {
        return isDisplayed(submitButton);
    }

    /**
     * Returns true if we are on the login page (submit button visible).
     *
     * @return true if on login page
     */
    public boolean isOnLoginPage() {
        return isDisplayed(submitButton);
    }
}
