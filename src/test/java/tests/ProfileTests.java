package tests;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import static utils.Config.*;

public class ProfileTests extends TestBase {

    @Test
    public void testEditProfileModalOpens() {
        WebDriver webDriver = getDriver();

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.openPage();
        loginPage.performLogIn(VALID_USERNAME_1, VALID_PASSWORD_1);

        HomePage homePage = new HomePage(webDriver);
        Assert.assertTrue(homePage.isUrlLoaded(), "The Home URL is not correct!");

        HeaderComponent header = new HeaderComponent(webDriver);
        header.clickProfile();

        ProfilePage profilePage = new ProfilePage(webDriver);
        Assert.assertTrue(profilePage.isUrlWithIDLoaded(VALID_USER_ID_1), "The Profile URL is not correct!");
        Assert.assertTrue(profilePage.isUsernameAsExpected(VALID_USERNAME_1));

        profilePage.clickEditProfileIcon();

        EditProfileModal editProfileModal = new EditProfileModal(webDriver);
        Assert.assertTrue(editProfileModal.isModalDisplayed(), "The Edit Profile Modal is not displayed!");

        // Close modal
        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.ESCAPE).perform();

        // Clean up - log out the user
        homePage.performLogOut();
    }

    @Test
    public void testUpdatePublicInfo() {
        WebDriver webDriver = getDriver();

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.openPage();
        loginPage.performLogIn(VALID_USERNAME_1, VALID_PASSWORD_1);

        HomePage homePage = new HomePage(webDriver);
        Assert.assertTrue(homePage.isUrlLoaded(), "The Home URL is not correct!");

        HeaderComponent header = new HeaderComponent(webDriver);
        header.clickProfile();

        ProfilePage profilePage = new ProfilePage(webDriver);
        Assert.assertTrue(profilePage.isUrlWithIDLoaded(VALID_USER_ID_1), "The Profile URL is not correct!");

        profilePage.clickEditProfileIcon();

        EditProfileModal editProfileModal = new EditProfileModal(webDriver);
        Assert.assertTrue(editProfileModal.isModalDisplayed(), "The Edit Profile Modal is not displayed!");

        String newPublicInfo = "This is a new public info! (TS: " +
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ")";

        editProfileModal.fillPublicInfo(newPublicInfo);
        editProfileModal.clickSaveButton();

        Assert.assertEquals(profilePage.getToastMessage().trim(), "Profile updated", "The toast message is not as expected!");

        Assert.assertTrue(profilePage.isPublicInfoAsExpected(newPublicInfo), "The public info is not updated!");

        // Close modal
        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.ESCAPE).perform();

        // Clean up - log out the user
        homePage.performLogOut();
    }
}
