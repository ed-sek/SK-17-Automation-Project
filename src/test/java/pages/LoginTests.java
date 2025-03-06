package pages;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.Config;

public class LoginTests extends TestBase {

    @Test
    public void verifyLoginPageURLIsLoaded() {
        WebDriver webDriver = getDriver();
        LoginPage loginPage = new LoginPage(webDriver);

        loginPage.openPage();

        Assert.assertTrue(loginPage.isUrlLoaded(), "The Login page is not loaded.");
    }

    @Test
    public void verifySignInFormIsDisplayed() {
        WebDriver webDriver = getDriver();
        LoginPage loginPage = new LoginPage(webDriver);

        loginPage.openPage();

        String expectedFormText = "Sign in";
        Assert.assertEquals(loginPage.getSignInFormText(), expectedFormText, "Login form is not displayed");
    }

    @Test
    public void verifyErrorMessageForInvalidLogin() {
        WebDriver webDriver = getDriver();
        LoginPage loginPage = new LoginPage(webDriver);

        loginPage.openPage();

        loginPage.performLogin(Config.INVALID_USERNAME, Config.INVALID_PASSWORD);

        String expectedMessage = "Wrong username or password!";
        Assert.assertEquals(loginPage.getSignInMessage().trim(), expectedMessage, "Sign in message is not as expected.");
    }

    @Test
    public void loginWithValidCredentials() {
        WebDriver webDriver = getDriver();
        LoginPage loginPage = new LoginPage(webDriver);
        HomePage homePage = new HomePage(webDriver);

        loginPage.openPage();

        loginPage.performLogin(Config.VALID_USERNAME, Config.VALID_PASSWORD);

        String expectedMessage = "Successful login!";
        Assert.assertEquals(loginPage.getSignInMessage().trim(), expectedMessage, "Sign in message is not as expected.");

        Assert.assertTrue(homePage.isUrlLoaded(), "Home page is not loaded.");
    }
}
