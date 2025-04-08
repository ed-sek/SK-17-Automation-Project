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
    public void testFollowUser() {
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

        Assert.assertTrue(profilePage.isFollowUnfollowButtonVisible(), "The Follow/Unfollow button is not visible!");
        String initialButtonText = profilePage.getFollowUnfollowButtonCurrentText();
        Assert.assertNotEquals(initialButtonText, "", "Could not get initial Follow/Unfollow button text.");

        // Determine the expected button text after clicking based on the initial text
        String expectedButtonTextAfterClick = initialButtonText.equals("Follow") ? "Unfollow" : "Follow";

        System.out.println("[before] Follow/Unfollow button text: " + initialButtonText);

        boolean textChanged = profilePage.clickFollowUnfollowAndWaitForTextChange(expectedButtonTextAfterClick);

        // Assert that the method reported success (text changed as expected)
        Assert.assertTrue(textChanged, "Follow/Unfollow button text did not change to '" + expectedButtonTextAfterClick + "' as expected!");

        // Optional: Get the text again *after* the wait for logging or double-checking
        String finalButtonText = profilePage.getFollowUnfollowButtonCurrentText();
        System.out.println("[after] Follow/Unfollow button text: " + finalButtonText);
        Assert.assertEquals(finalButtonText, expectedButtonTextAfterClick, "Button text after wait does not match expected.");

        // Clean up - log out the user
        homePage.performLogOut();
    }

    @Test
    public void testCommentOnPost() {
        WebDriver webDriver = getDriver();

        // Log in as User A
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.openPage();
        loginPage.performLogIn(VALID_USERNAME_1, VALID_PASSWORD_1);

        HomePage homePage = new HomePage(webDriver);
        //Assert.assertTrue(homePage.isUrlLoaded(), "The Home URL is not correct!");

        HeaderComponent header = new HeaderComponent(webDriver);
        header.populateSearchField(VALID_USERNAME_3);

        header.waitAndClickSearchDropdownUser(VALID_USERNAME_3);

        ProfilePage profilePage = new ProfilePage(webDriver);

        Assert.assertTrue(profilePage.isUrlWithIDLoaded(VALID_USER_ID_3), "The Profile URL is not correct!");

        // Get current state and Follow the user if not already followed
        String followUnfollowButtonText = profilePage.getFollowUnfollowButtonCurrentText();
        if (followUnfollowButtonText.equals("Follow")) {
            boolean followed = profilePage.clickFollowUnfollowAndWaitForTextChange("Unfollow");
            Assert.assertTrue(followed, "Failed to follow the user before commenting.");
        }

        profilePage.clickAllPostsFilterButton();

        // Select on which post to comment on (ensure posts are loaded)
        Assert.assertTrue(profilePage.waitForPostCountToBeGreaterThan(0), "No posts found on profile to comment on.");
        profilePage.clickPost(0); // Click the desired post (zero-based index, adjust as needed)

        PostModal postModal = new PostModal(webDriver);
        Assert.assertTrue(postModal.isModalVisible(), "The post modal is not visible!");

        String newCommentText = "This is a new comment (TS: " +
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ")";

        postModal.postComment(newCommentText);

        Assert.assertTrue(postModal.waitForCommentPresent(newCommentText), "New comment did not appear within the timeout!");

        // Close modal
        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.ESCAPE).perform();

        // Clean up - log out the user
        homePage.performLogOut();
    }
}