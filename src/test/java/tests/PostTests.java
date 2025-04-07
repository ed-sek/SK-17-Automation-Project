package tests;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import java.io.File;

import static utils.Config.*;

public class PostTests extends TestBase {

    private static final String UPLOAD_PICTURE_FILENAME = "testUpload-SpiderMan.jpg";

    @Test
    public void testCreatePost() {
        File file = new File(UPLOAD_DIR.concat(UPLOAD_PICTURE_FILENAME));
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

        int countOfPosts = profilePage.getPostCount(); // Initial count

        String caption = String.format("Testing create post caption: %d", countOfPosts + 1);
        header.clickNewPost();

        PostPage postPage = new PostPage(webDriver);
        Assert.assertTrue(postPage.isUrlLoaded(), "The POST URL is not correct!");
        postPage.uploadPicture(file);
        Assert.assertTrue(postPage.isImageVisible(), "The image is not visible!");
        Assert.assertEquals(file.getName(), postPage.getImageName(), "The image name is incorrect!");
        postPage.populatePostCaption(caption);
        postPage.clickCreatePost();

        Assert.assertTrue(profilePage.isUrlWithIDLoaded(VALID_USER_ID_1), "Profile page didn't load after creating post.");

        webDriver.navigate().refresh();
        profilePage.waitForLoaderToDisappear();

        Actions actions = new Actions(webDriver);
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();

        // Assert the post list container is loaded after refresh/wait
        Assert.assertTrue(profilePage.isAppPostListLoaded(), "The app post list is not loaded after refresh!");

        int expectedPostCount = countOfPosts + 1;
        Assert.assertTrue(profilePage.waitForPostCountToBe(expectedPostCount),
                "Post count did not update to " + expectedPostCount + " after creation.");

        // Clean up - log out the user
        homePage.performLogOut();
    }

    @Test
    public void testDeletePost() {
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

        int initialPostCount = profilePage.getPostCount();

        if (initialPostCount == 0) {
            header.clickNewPost();

            PostPage postPage = new PostPage(webDriver);
            Assert.assertTrue(postPage.isUrlLoaded(), "The POST URL is not correct!");
            postPage.uploadPicture(new File(UPLOAD_DIR.concat(UPLOAD_PICTURE_FILENAME)));
            postPage.populatePostCaption("Caption for delete post test");
            postPage.clickCreatePost();

            Assert.assertEquals(homePage.getToastMessage().trim(), "Post created!", "The toast message is incorrect!");
            initialPostCount = profilePage.getPostCount(); // Re-get count
        }

        // Click the last post to delete it
        profilePage.clickPost(initialPostCount - 1);

        PostModal postModal = new PostModal(webDriver);
        Assert.assertTrue(postModal.isModalVisible(), "Post modal didn't appear.");
        Assert.assertTrue(postModal.isImageVisible(), "The image is not visible!");

        postModal.clickDeletePost();
        postModal.confirmDeletePost();


        webDriver.navigate().refresh();
        profilePage.waitForLoaderToDisappear();

        // Scroll down if necessary
        Actions actions = new Actions(webDriver);
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();

        int expectedPostCountAfterDelete = initialPostCount - 1;
        Assert.assertTrue(profilePage.waitForPostCountToBe(expectedPostCountAfterDelete),
                "Post count did not update to " + expectedPostCountAfterDelete + " after deletion.");

        // Clean up - log out the user
        homePage.performLogOut();
    }

    @Test
    public void testPostVisibilityChange() {
        File file = new File(UPLOAD_DIR.concat(UPLOAD_PICTURE_FILENAME));
        WebDriver webDriver = getDriver();

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.openPage();
        loginPage.performLogIn(VALID_USERNAME_1, VALID_PASSWORD_1);

        HomePage homePage = new HomePage(webDriver);
        Assert.assertTrue(homePage.isUrlLoaded(), "The Home URL is not correct!");

        HeaderComponent header = new HeaderComponent(webDriver);
        header.clickProfile();

        ProfilePage profilePage = new ProfilePage(webDriver);
        Assert.assertTrue(profilePage.isUrlWithIDLoaded(VALID_USER_ID_1), "Profile page did not load.");

        int countOfPosts = profilePage.getPostCount();

        if (countOfPosts == 0) {
            // Create a post first
            header.clickNewPost();
            PostPage postPage = new PostPage(webDriver);
            Assert.assertTrue(postPage.isUrlLoaded(), "The POST URL is not correct!");
            postPage.uploadPicture(file);
            Assert.assertTrue(postPage.isImageVisible(), "The image is not visible!");
            Assert.assertEquals(file.getName(), postPage.getImageName(), "The image name is incorrect!");

            String caption = String.format("Testing create post caption: %d", countOfPosts + 1);
            postPage.populatePostCaption(caption);
            postPage.clickCreatePost();

            Assert.assertTrue(profilePage.isUrlWithIDLoaded(VALID_USER_ID_1), "Profile page didn't load after creating post.");

            Assert.assertTrue(profilePage.waitForPostCountToBe(1), "Post count did not become 1 after creation.");
            countOfPosts = profilePage.getPostCount(); // Re-get count
        }

        profilePage.clickPost(0); // Click on the first post

        PostModal postModal = new PostModal(webDriver);
        Assert.assertTrue(postModal.isModalVisible(), "The modal element is not visible!");

        Actions actions = new Actions(webDriver);
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();

        boolean isLocked = postModal.isPadlockLocked();
        postModal.clickPadlock();

        Assert.assertNotEquals(postModal.isPadlockLocked(), isLocked, "The padlock state did not toggle!");

        // Close modal
        actions.sendKeys(Keys.ESCAPE).perform();

        // Clean up - log out the user
        homePage.performLogOut();
    }
}