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
 * TC1  - Positive login: valid credentials → success page
 * TC2  - Negative: invalid username → error message
 * TC3  - Negative: invalid password → error message
 * TC4  - Empty username only → error message
 * TC5  - Empty password only → error message
 * TC6  - Both fields empty → error message
 * TC7  - Username case sensitivity (STUDENT) → error message
 * TC8  - Logout after valid login → redirected back to login page
 * TC9  - Page title verification
 * TC10 - Login page UI elements visible on load
 */
public class LoginTest extends BaseTest {

    // ----------------------------------------------------------------
    // Data Providers
    // ----------------------------------------------------------------

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

    // ----------------------------------------------------------------
    // TC1 — Positive Login
    // ----------------------------------------------------------------

    /**
     * TC1: Valid credentials → success page, success text, Log out button visible.
     */
    @Test(dataProvider = "validCredentials",
          description = "TC1 - Positive login with valid credentials",
          groups = {"smoke", "regression"})
    public void testValidLogin(String username, String password) {
        ExtentReportManager.logInfo("TC1: Logging in with valid credentials — user: " + username);

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);

        LoggedInPage loggedInPage = new LoggedInPage(getDriver());

        Assert.assertTrue(loggedInPage.isOnSuccessPage(),
                "URL should contain 'logged-in-successfully'. Actual: " + loggedInPage.getCurrentUrl());
        ExtentReportManager.logPass("URL verified: contains 'logged-in-successfully'");

        String successText = loggedInPage.getSuccessMessage();
        boolean hasSuccessText = successText.contains("Congratulations")
                || successText.toLowerCase().contains("successfully logged in")
                || successText.toLowerCase().contains("logged in successfully");
        Assert.assertTrue(hasSuccessText,
                "Success message mismatch. Got: " + successText);
        ExtentReportManager.logPass("Success message verified: " + successText);

        Assert.assertTrue(loggedInPage.isLogOutButtonDisplayed(),
                "Log out button should be visible after login");
        ExtentReportManager.logPass("Log out button is displayed");
    }

    // ----------------------------------------------------------------
    // TC2 & TC3 — Negative Login
    // ----------------------------------------------------------------

    /**
     * TC2: Invalid username → "Your username is invalid!"
     * TC3: Invalid password → "Your password is invalid!"
     */
    @Test(dataProvider = "invalidCredentials",
          description = "TC2/TC3 - Negative login: invalid username or password",
          groups = {"regression"})
    public void testInvalidLogin(String username, String password, String expectedError) {
        ExtentReportManager.logInfo("Attempting login — user: " + username + " | pass: " + password);

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed");
        ExtentReportManager.logPass("Error message is displayed");

        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, expectedError,
                "Error mismatch. Expected: [" + expectedError + "] Got: [" + actualError + "]");
        ExtentReportManager.logPass("Error message verified: " + actualError);
    }

    // ----------------------------------------------------------------
    // TC4 — Empty Username Only
    // ----------------------------------------------------------------

    /**
     * TC4: Password filled, username empty → error shown.
     */
    @Test(description = "TC4 - Submit with empty username field only",
          groups = {"regression"})
    public void testEmptyUsernameOnly() {
        ExtentReportManager.logInfo("TC4: Submitting with empty username and valid password");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterPassword("Password123");
        loginPage.clickSubmit();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error should appear when username is empty");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Username is required"),
                "Expected 'Username is required'. Got: " + error);
        ExtentReportManager.logPass("Correct error for empty username: " + error);
    }

    // ----------------------------------------------------------------
    // TC5 — Empty Password Only
    // ----------------------------------------------------------------

    /**
     * TC5: Username filled, password empty → error shown.
     */
    @Test(description = "TC5 - Submit with empty password field only",
          groups = {"regression"})
    public void testEmptyPasswordOnly() {
        ExtentReportManager.logInfo("TC5: Submitting with valid username and empty password");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterUsername("student");
        loginPage.clickSubmit();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error should appear when password is empty");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Password is required"),
                "Expected 'Password is required'. Got: " + error);
        ExtentReportManager.logPass("Correct error for empty password: " + error);
    }

    // ----------------------------------------------------------------
    // TC6 — Both Fields Empty
    // ----------------------------------------------------------------

    /**
     * TC6: Both fields empty → username required error.
     */
    @Test(description = "TC6 - Submit with both fields empty",
          groups = {"regression"})
    public void testBothFieldsEmpty() {
        ExtentReportManager.logInfo("TC6: Submitting login form with both fields empty");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.clickSubmit();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error should appear when both fields are empty");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Username is required"),
                "Expected 'Username is required'. Got: " + error);
        ExtentReportManager.logPass("Both empty fields correctly rejected: " + error);
    }

    // ----------------------------------------------------------------
    // TC7 — Case Sensitivity
    // ----------------------------------------------------------------

    /**
     * TC7: Username in uppercase (STUDENT) should fail — login is case-sensitive.
     */
    @Test(description = "TC7 - Username is case-sensitive (STUDENT should fail)",
          groups = {"regression"})
    public void testUsernameCaseSensitivity() {
        ExtentReportManager.logInfo("TC7: Attempting login with uppercase username STUDENT");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("STUDENT", "Password123");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Uppercase username should be rejected");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Your username is invalid!"),
                "Expected invalid username error. Got: " + error);
        ExtentReportManager.logPass("Case sensitivity verified — STUDENT correctly rejected: " + error);
    }

    // ----------------------------------------------------------------
    // TC8 — Logout
    // ----------------------------------------------------------------

    /**
     * TC8: Valid login → click Log out → redirected back to login page.
     */
    @Test(description = "TC8 - Logout after successful login redirects to login page",
          groups = {"smoke", "regression"})
    public void testLogout() {
        ExtentReportManager.logInfo("TC8: Logging in, then clicking Log out");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("student", "Password123");

        LoggedInPage loggedInPage = new LoggedInPage(getDriver());
        Assert.assertTrue(loggedInPage.isLogOutButtonDisplayed(), "Log out button should be visible");
        ExtentReportManager.logInfo("Logged in — clicking Log out");

        loginPage = loggedInPage.clickLogOut(getDriver());

        Assert.assertTrue(loginPage.isOnLoginPage(),
                "Should be back on login page after logout. URL: " + loginPage.getCurrentUrl());
        ExtentReportManager.logPass("Logout successful — back on login page");
    }

    // ----------------------------------------------------------------
    // TC9 — Page Title
    // ----------------------------------------------------------------

    /**
     * TC9: Login page title should contain "Test Login".
     */
    @Test(description = "TC9 - Verify login page title",
          groups = {"smoke", "regression"})
    public void testLoginPageTitle() {
        ExtentReportManager.logInfo("TC9: Verifying login page title");

        LoginPage loginPage = new LoginPage(getDriver());
        String title = loginPage.getPageTitle();

        Assert.assertTrue(title.contains("Test Login"),
                "Page title should contain 'Test Login'. Got: " + title);
        ExtentReportManager.logPass("Page title verified: " + title);
    }

    // ----------------------------------------------------------------
    // TC10 — UI Elements Visible on Load
    // ----------------------------------------------------------------

    /**
     * TC10: Username field, password field, and submit button all visible on load.
     */
    @Test(description = "TC10 - Verify all login UI elements are visible on page load",
          groups = {"smoke", "regression"})
    public void testLoginPageUIElements() {
        ExtentReportManager.logInfo("TC10: Verifying login page UI elements are present");

        LoginPage loginPage = new LoginPage(getDriver());

        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(),
                "Username field should be visible");
        ExtentReportManager.logPass("Username field ✅");

        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(),
                "Password field should be visible");
        ExtentReportManager.logPass("Password field ✅");

        Assert.assertTrue(loginPage.isSubmitButtonDisplayed(),
                "Submit button should be visible");
        ExtentReportManager.logPass("Submit button ✅");
    }
}
