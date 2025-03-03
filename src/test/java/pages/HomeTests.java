package pages;

import ch.qos.logback.core.util.DatePatternToRegexUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import utils.Config;

import java.time.Duration;

public class HomeTests {
    private WebDriver webDriver;
    private HomePage homePage;

    @BeforeSuite
    protected final void setUpTestSuite() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    protected final void setUpTest() {
        this.webDriver = new ChromeDriver();
        this.webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        this.webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        this.webDriver.manage().window().maximize();
        this.webDriver.navigate().to(Config.HOME_PAGE_URL);

        this.homePage = new HomePage(this.webDriver);
    }

    @AfterSuite
    public void tearDownTestSuite() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    public void verifyHomePageURLIsLoaded() {
        Assert.assertTrue(homePage.isUrlLoaded(), "The Home page is not loaded.");
    }
}
