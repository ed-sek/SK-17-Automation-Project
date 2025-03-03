package pages;

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

public class SignUpTests {

    private WebDriver webDriver;
    private SignUpPage signUpPage;


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
        this.webDriver.navigate().to(Config.SIGNUP_PAGE_URL);

        this.signUpPage = new SignUpPage(this.webDriver);
    }

    @AfterSuite
    public void tearDownTestSuite() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    public void verifySignUpPageURLIsLoaded() {
        Assert.assertTrue(signUpPage.isUrlLoaded(), "The Sign up page is not loaded.");
    }

    @Test
    public void verifySignUpFormIsDisplayed() {
        String expectedFormText = "Sign up";
        Assert.assertEquals(signUpPage.getSignUpFormText(), expectedFormText, "Sign up form is not displayed");
    }

}
