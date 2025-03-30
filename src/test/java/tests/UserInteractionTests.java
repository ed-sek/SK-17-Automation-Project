package tests;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import static utils.Config.*;

public class UserInteractionTests extends TestBase {

    @Test
    public void testCommentOnPost() throws InterruptedException {
        WebDriver webDriver = getDriver();

        // Log in as User A
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.openPage();
        loginPage.performLogIn(VALID_USERNAME_1, VALID_PASSWORD_1);

        HomePage homePage = new HomePage(webDriver);
        Assert.assertTrue(homePage.isUrlLoaded(), "The Home URL is not correct!");

        HeaderComponent header = new HeaderComponent(webDriver);
        header.populateSearchField(VALID_USERNAME_3);

        header.waitAndClickSearchDropdownUser(VALID_USERNAME_3);


        ProfilePage profilePage = new ProfilePage(webDriver);

        Thread.sleep(5000);


        Assert.assertTrue(profilePage.isUrlWithIDLoaded(VALID_USER_ID_3), "The Profile URL is not correct!");

        profilePage.clickAllPostsFilterButton();

        // Select on which post to comment on
        profilePage.clickPost(1);

        PostModal postModal = new PostModal(webDriver);
        Assert.assertTrue(postModal.isModalVisible(), "The post modal is not visible!");

        String newCommentText = "This is a new comment (TS: " +
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ")";

        postModal.postComment(newCommentText);

        Thread.sleep(5000);

        Assert.assertTrue(postModal.isCommentPresent(newCommentText), "New comment not found!");

        // Close modal
        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.ESCAPE).perform();

        // Clean up - log out the user
        homePage.performLogOut();
    }

    @Test
    public void testFollowUer() throws InterruptedException {
        WebDriver webDriver = getDriver();

        // Log in as User A
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.openPage();
        loginPage.performLogIn(VALID_USERNAME_1, VALID_PASSWORD_1);

        HomePage homePage = new HomePage(webDriver);
        Assert.assertTrue(homePage.isUrlLoaded(), "The Home URL is not correct!");

        HeaderComponent header = new HeaderComponent(webDriver);
        header.populateSearchField(VALID_USERNAME_3);

        header.waitAndClickSearchDropdownUser(VALID_USERNAME_3);

        ProfilePage profilePage = new ProfilePage(webDriver);

        Assert.assertTrue(profilePage.isUrlWithIDLoaded(VALID_USER_ID_3), "The Profile URL is not correct!");

        String followUnfollowButtonText = profilePage.getFollowUnfollowButtonCurrentText();

        System.out.println("[before] Follow/Unfollow button text: " + followUnfollowButtonText);

        profilePage.clickFollowUnfollowButton();

        Thread.sleep(8000);

        Assert.assertTrue(profilePage.isFollowUnfollowButtonVisible(), "The Follow/Unfollow button is not visible!");

        String newFollowUnfollowButtonText = profilePage.getFollowUnfollowButtonCurrentText();

        System.out.println("[after] Follow/Unfollow button text: " + newFollowUnfollowButtonText);

        Thread.sleep(5000);

        Assert.assertNotEquals(followUnfollowButtonText, newFollowUnfollowButtonText, "The Follow/Unfollow button text is not changed!");

        // Clean up - log out the user
        homePage.performLogOut();
    }
}
