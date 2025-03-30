package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SignUpPage;

import java.util.Locale;

public class RegistrationTests extends TestBase {

    @Test
    public void testSuccessfulRegistration()  {
        WebDriver webDriver = getDriver();

        SignUpPage signUpPage = new SignUpPage(webDriver);
        signUpPage.openPage();
        Assert.assertTrue(signUpPage.isUrlLoaded(), "Sign-Up page did not load.");

        // Generate a short, unique suffix based on the current time
        long timestamp = System.currentTimeMillis();
        String uniqueSuffix = Long.toHexString(timestamp).substring(0, 8).toUpperCase(Locale.ROOT); //take first 8 hex digits.

        String newUsername = "Hero" + uniqueSuffix;
        String newEmail = "hero" + uniqueSuffix + "@t.com";
        String newPublicInfo = "Public info updated (UID: " + uniqueSuffix + ")";

        signUpPage.populateUsername(newUsername);
        signUpPage.populateEmail(newEmail);
        signUpPage.populateBirthDate("01/01/1990");
        signUpPage.populatePassword("PassPass1");
        signUpPage.populateConfirmPassword("PassPass1");
        signUpPage.populatePublicInfo(newPublicInfo);

        signUpPage.clickSignIn();

        Assert.assertEquals(signUpPage.getToastMessage().trim(), "Successful register!", "Registration did not succeed as expected.");
    }
}
