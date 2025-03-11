package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HeaderComponent;
import pages.HomePage;
import pages.LoginPage;
import pages.PostPage;
import utils.Config;

import java.io.File;

public class PostTests extends TestBase {

    private static final String UPLOAD_PICTURE_FILENAME = "testUpload-SpiderMan.jpg";

    @Test
    public void testCreatePost() throws InterruptedException {
        // Define parameters in scope
        String password = Config.VALID_PASSWORD;
        String username = Config.VALID_USERNAME;
        File file = new File(UPLOAD_DIR.concat(UPLOAD_PICTURE_FILENAME));
        String caption = "Testing create post caption-4";

        WebDriver webDriver = getDriver();

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.openPage();
        loginPage.performLogin(username, password);

        HomePage homePage = new HomePage(webDriver);
        Assert.assertTrue(homePage.isUrlLoaded(), "The Home URL is not correct!");

        HeaderComponent header = new HeaderComponent(webDriver);
        header.clickNewPost();

        PostPage postPage = new PostPage(webDriver);
        Assert.assertTrue(postPage.isUrlLoaded(), "The POST URL is not correct!");

        postPage.uploadPicture(file);
        Assert.assertTrue(postPage.isImageVisible(), "The image is not visible!");
        Assert.assertEquals(file.getName(), postPage.getImageName(), "The image name is incorrect!");
        postPage.populatePostCaption(caption);
        postPage.clickCreatePost();

        // TODO: Implement validations here after creating ProfilePage page object

        // Temporary delay to allow post creation before WebDriver quits, until ProfilePage functionality is implemented.
        Thread.sleep(10000);
    }
}
