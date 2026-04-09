package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for https://practicetestautomation.com/logged-in-successfully/
 */
public class LoggedInPage extends BasePage {

    @FindBy(css = "a.wp-block-button__link")
    private WebElement logOutButton;

    @FindBy(css = "h1.post-title")
    private WebElement successMessage;

    /**
     * Initializes LoggedInPage with the given driver.
     *
     * @param driver WebDriver instance
     */
    public LoggedInPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Returns true if the Log out button is displayed.
     *
     * @return true if visible
     */
    public boolean isLogOutButtonDisplayed() {
        return isDisplayed(logOutButton);
    }

    /**
     * Returns the success heading text.
     *
     * @return heading text
     */
    public String getSuccessMessage() {
        return getText(successMessage);
    }

    /**
     * Returns true if the current URL contains the success path.
     *
     * @return true if on logged-in-successfully page
     */
    public boolean isOnSuccessPage() {
        return getCurrentUrl().contains("logged-in-successfully");
    }

    /**
     * Clicks the Log out button and returns the LoginPage.
     *
     * @param driver WebDriver instance
     * @return LoginPage instance after logout
     */
    public LoginPage clickLogOut(WebDriver driver) {
        click(logOutButton);
        return new LoginPage(driver);
    }
}
