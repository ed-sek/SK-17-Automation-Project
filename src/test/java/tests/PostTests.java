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
        int countOfPosts = profilePage.getPostCount();

        String caption = String.format("Testing create post caption: %d", countOfPosts + 1);
        header.clickNewPost();

        PostPage postPage = new PostPage(webDriver);
        Assert.assertTrue(postPage.isUrlLoaded(), "The POST URL is not correct!");
        postPage.uploadPicture(file);
        Assert.assertTrue(postPage.isImageVisible(), "The image is not visible!");
        Assert.assertEquals(file.getName(), postPage.getImageName(), "The image name is incorrect!");
        postPage.populatePostCaption(caption);
        postPage.clickCreatePost();

        Assert.assertTrue(profilePage.isUrlWithIDLoaded(VALID_USER_ID_1), "The Profile URL is not correct!");

        Assert.assertEquals(profilePage.getPostCount(), countOfPosts + 1, "The number of Posts is incorrect!");
        profilePage.clickPost(countOfPosts);

        PostModal postModal = new PostModal(webDriver);
        Assert.assertTrue(postModal.isImageVisible(), "The image is not visible!");
        Assert.assertEquals(postModal.getPostTitle(), caption);
        Assert.assertEquals(postModal.getPostUser(), VALID_USERNAME_1);

        // Close modal
        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.ESCAPE).perform();

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
            postPage.populatePostCaption("Testing delete post caption");
            postPage.clickCreatePost();

            Assert.assertEquals(homePage.getToastMessage().trim(), "Post created!", "The toast message is incorrect!");
            initialPostCount = profilePage.getPostCount();
        }

        profilePage.clickPost(initialPostCount - 1);

        PostModal postModal = new PostModal(webDriver);
        Assert.assertTrue(postModal.isImageVisible(), "The image is not visible!");

        postModal.clickDeletePost();
        postModal.confirmDeletePost();

        Assert.assertEquals(profilePage.getToastMessage().trim(), "Post Deleted!", "The toast message is not as expected!");

        webDriver.navigate().refresh();
        profilePage = new ProfilePage(webDriver);

        int finalPostCount = profilePage.getPostCount();

        Assert.assertEquals(finalPostCount, initialPostCount - 1, "The number of Posts is incorrect!");

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
        int countOfPosts = profilePage.getPostCount();

        String caption = String.format("Testing create post caption: %d", countOfPosts + 1);
        header.clickNewPost();

        PostPage postPage = new PostPage(webDriver);
        Assert.assertTrue(postPage.isUrlLoaded(), "The POST URL is not correct!");
        postPage.uploadPicture(file);
        Assert.assertTrue(postPage.isImageVisible(), "The image is not visible!");
        Assert.assertEquals(file.getName(), postPage.getImageName(), "The image name is incorrect!");
        postPage.populatePostCaption(caption);
        postPage.clickCreatePost();

        Assert.assertTrue(profilePage.isUrlWithIDLoaded(VALID_USER_ID_1), "The Profile URL is not correct!");

        int intermediatePostCount = profilePage.getPostCount();


        Assert.assertEquals(intermediatePostCount, countOfPosts + 1, "The number of Posts is incorrect!");

        profilePage.clickPost(countOfPosts);

        PostModal postModal = new PostModal(webDriver);
        Assert.assertTrue(postModal.isModalVisible(), "The modal element is not visible!");

        Assert.assertTrue(postModal.isPadlockUnlocked(), "The padlock element is not in unlocked state!");

        postModal.clickPadlock();

        Assert.assertTrue(postModal.isPadlockLocked(), "The padlock element is not in locked state!");

        // Close modal
        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.ESCAPE).perform();

        webDriver.navigate().refresh();
        profilePage = new ProfilePage(webDriver);

        int finalPostCount = profilePage.getPostCount();

        Assert.assertEquals(finalPostCount, intermediatePostCount - 1, "The number of visible posts is incorrect!");

        // Clean up - log out the user
        homePage.performLogOut();

    }
}
