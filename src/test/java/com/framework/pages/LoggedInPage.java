package com.framework.pages;

import com.framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for the post-login success page.
 * URL: https://practicetestautomation.com/logged-in-successfully/
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
     * @return true if Log out is visible
     */
    public boolean isLogOutButtonDisplayed() {
        return isDisplayed(logOutButton);
    }

    /**
     * Returns the success message text.
     *
     * @return success message string
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
}
