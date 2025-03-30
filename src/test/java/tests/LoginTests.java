package tests;

import org.openqa.selenium.WebDriver;

import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;

import static utils.Config.*;

public class LoginTests extends TestBase {

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginDataProvider() {
        return new Object[][]{
                {INVALID_USERNAME, INVALID_PASSWORD, "Wrong username or password!"}, // Wrong credentials
                {VALID_USERNAME_1, INVALID_PASSWORD, "Wrong username or password!"} // Wrong password for valid user
        };
    }

    @Test
    public void testValidLogin() {
        WebDriver webDriver = getDriver();

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.openPage();
        loginPage.performLogIn(VALID_USERNAME_1, VALID_PASSWORD_1);

        HomePage homePage = new HomePage(webDriver);
        Assert.assertTrue(homePage.isUrlLoaded(), "Home page did not load after login.");
    }

    @Test(dataProvider = "invalidLoginData")
    public void testUnsuccessfulLoginErrorMessages(String username, String password, String expectedErrorMessage) {
        WebDriver webDriver = getDriver();

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.openPage();

        loginPage.performLogIn(username, password);

        String actualErrorMessage = loginPage.getToastMessage();

        Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message mismatch!");
    }
}
