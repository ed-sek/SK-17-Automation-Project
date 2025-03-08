package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SignUpPage;

public class SignUpTests extends TestBase {

    @Test
    public void verifySignUpPageURLIsLoaded() {
        WebDriver webDriver = getDriver();
        SignUpPage signUpPage = new SignUpPage(webDriver);

        signUpPage.openPage();

        Assert.assertTrue(signUpPage.isUrlLoaded(), "The Sign up page is not loaded.");
    }

    @Test
    public void verifySignUpFormIsDisplayed() {
        WebDriver webDriver = getDriver();
        SignUpPage signUpPage = new SignUpPage(webDriver);

        signUpPage.openPage();

        String expectedFormText = "Sign up";
        Assert.assertEquals(signUpPage.getSignUpFormText(), expectedFormText, "Sign up form is not displayed");
    }
}
