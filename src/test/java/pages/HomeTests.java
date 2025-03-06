package pages;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomeTests extends TestBase {

    @Test
    public void verifyHomePageURLIsLoaded() {
        WebDriver webDriver = getDriver();
        HomePage homePage = new HomePage(webDriver);

        homePage.openPage();

        Assert.assertTrue(homePage.isUrlLoaded(), "The Home page is not loaded.");
    }
}
