package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.pages.LoggedInPage;
import com.framework.pages.LoginPage;
import com.framework.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test class for https://practicetestautomation.com/practice-test-login/
 *
 * Test Cases:
 *   TC1 - Positive login: valid credentials → success page
 *   TC2 - Negative: invalid username → error message
 *   TC3 - Negative: invalid password → error message
 */
public class LoginTest extends BaseTest {

    // ---- Data Providers ----

    @DataProvider(name = "validCredentials")
    public Object[][] validCredentials() {
        return new Object[][] {
            { "student", "Password123" }
        };
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][] {
            { "incorrectUser",  "Password123",      "Your username is invalid!" },
            { "student",        "incorrectPassword", "Your password is invalid!" }
        };
    }

    // ---- Test Methods ----

    /**
     * TC1 - Positive LogIn test.
     * Steps:
     * 1. Open login page
     * 2. Enter username: student
     * 3. Enter password: Password123
     * 4. Click Submit
     * 5. Verify URL contains "logged-in-successfully"
     * 6. Verify success message contains "Congratulations" or "successfully logged in"
     * 7. Verify Log out button is displayed
     */
    @Test(dataProvider = "validCredentials",
          description = "TC1 - Positive login with valid credentials",
          groups = {"smoke", "regression"})
    public void testValidLogin(String username, String password) {
        ExtentReportManager.logInfo("TC1: Opening login page");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);

        ExtentReportManager.logInfo("Credentials entered and Submit clicked");

        LoggedInPage loggedInPage = new LoggedInPage(getDriver());

        // Verify URL
        Assert.assertTrue(loggedInPage.isOnSuccessPage(),
                "URL should contain 'logged-in-successfully'. Actual: " + loggedInPage.getCurrentUrl());
        ExtentReportManager.logPass("URL verified: contains 'logged-in-successfully'");

        // Verify success message
        String successText = loggedInPage.getSuccessMessage();
        boolean hasSuccessText = successText.contains("Congratulations")
                || successText.toLowerCase().contains("successfully logged in");
        Assert.assertTrue(hasSuccessText,
                "Success message should contain 'Congratulations' or 'successfully logged in'. Got: " + successText);
        ExtentReportManager.logPass("Success message verified: " + successText);

        // Verify Log out button
        Assert.assertTrue(loggedInPage.isLogOutButtonDisplayed(),
                "Log out button should be visible after login");
        ExtentReportManager.logPass("Log out button is displayed");
    }

    /**
     * TC2 - Negative username test.
     * Steps:
     * 1. Open login page
     * 2. Enter username: incorrectUser
     * 3. Enter password: Password123
     * 4. Click Submit
     * 5. Verify error message is displayed
     * 6. Verify error text is "Your username is invalid!"
     */
    @Test(dataProvider = "invalidCredentials",
          description = "TC2/TC3 - Negative login with invalid credentials",
          groups = {"regression"})
    public void testInvalidLogin(String username, String password, String expectedError) {
        ExtentReportManager.logInfo("Attempting login with username: " + username);

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);

        ExtentReportManager.logInfo("Checking error message...");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for invalid credentials");
        ExtentReportManager.logPass("Error message is displayed");

        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, expectedError,
                "Error message mismatch. Expected: [" + expectedError + "] Got: [" + actualError + "]");
        ExtentReportManager.logPass("Error message verified: " + actualError);
    }
}
