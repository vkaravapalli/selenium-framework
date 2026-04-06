package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.pages.LoginPage;
import com.framework.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test class for Login functionality on https://www.saucedemo.com.
 */
public class LoginTest extends BaseTest {

    // ---- Data Providers ----

    /**
     * Valid credentials for saucedemo.
     */
    @DataProvider(name = "validCredentials")
    public Object[][] validCredentials() {
        return new Object[][] {
            { "standard_user", "secret_sauce" }
        };
    }

    /**
     * Invalid credential combinations.
     */
    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][] {
            { "invalid_user",   "wrong_password",  "Epic sadface: Username and password do not match any user in this service" },
            { "locked_out_user","secret_sauce",    "Epic sadface: Sorry, this user has been locked out." }
        };
    }

    // ---- Test Methods ----

    /**
     * Verifies that a valid user can log in successfully.
     */
    @Test(dataProvider = "validCredentials",
          description = "Verify successful login with valid credentials",
          groups = {"smoke", "regression"})
    public void testValidLogin(String username, String password) {
        ExtentReportManager.logInfo("Attempting login with user: " + username);

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);

        ExtentReportManager.logInfo("Login submitted. Checking result...");
        Assert.assertTrue(loginPage.isLoginSuccessful(),
                "Login should succeed for user: " + username);

        ExtentReportManager.logPass("Valid login succeeded for: " + username);
    }

    /**
     * Verifies that invalid credentials show the correct error message.
     */
    @Test(dataProvider = "invalidCredentials",
          description = "Verify error message displayed for invalid credentials",
          groups = {"regression"})
    public void testInvalidLogin(String username, String password, String expectedError) {
        ExtentReportManager.logInfo("Attempting login with invalid user: " + username);

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);

        ExtentReportManager.logInfo("Checking for error message...");
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for invalid login");

        String actualError = loginPage.getErrorMessage();
        Assert.assertTrue(actualError.contains(expectedError.substring(0, 30)),
                "Error message mismatch. Expected to contain: " + expectedError
                + " | Got: " + actualError);

        ExtentReportManager.logPass("Correct error shown for invalid credentials: " + username);
    }

    /**
     * Verifies that submitting empty credentials shows an error.
     */
    @Test(description = "Verify error message when credentials are empty",
          groups = {"regression"})
    public void testEmptyCredentials() {
        ExtentReportManager.logInfo("Submitting login form with empty fields");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should appear for empty credentials");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Username is required"),
                "Expected 'Username is required' error. Got: " + error);

        ExtentReportManager.logPass("Empty credentials correctly rejected with error: " + error);
    }
}
