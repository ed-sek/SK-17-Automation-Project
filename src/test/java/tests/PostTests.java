package tests;

import org.openqa.selenium.WebDriver;
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
        loginPage.performLogIn(VALID_USERNAME, VALID_PASSWORD);

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

        Assert.assertTrue(profilePage.isUrlWithIDLoaded(9223), "The Profile URL is not correct!");

        Assert.assertEquals(profilePage.getPostCount(), countOfPosts + 1, "The number of Posts is incorrect!");
        profilePage.clickPost(countOfPosts);

        PostModalComponent postModal = new PostModalComponent(webDriver);
        Assert.assertTrue(postModal.isImageVisible(), "The image is not visible!");
        Assert.assertEquals(postModal.getPostTitle(), caption);
        Assert.assertEquals(postModal.getPostUser(), VALID_USERNAME);

    }

}
