package pages;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.Config;

import java.time.Duration;

public class LoginTests {

    private WebDriver webDriver;
    private LoginPage loginPage;

    @BeforeSuite
    protected final void setUpTestSuite() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    protected final void setupTest() {
        this.webDriver = new ChromeDriver();
        this.webDriver.manage().window().maximize();
        this.webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        this.webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        this.webDriver.navigate().to(Config.LOGIN_PAGE_URL);

        this.loginPage = new LoginPage(this.webDriver);
    }

    @AfterSuite
    public void tearDownTestSuite() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    public void verifyLoginPageURLIsLoaded() {
        Assert.assertTrue(loginPage.isUrlLoaded(), "The login page is not loaded.");
    }

    @Test
    public void verifySignInFormIsDisplayed() {
        Assert.assertEquals(loginPage.getSignInFormText(), "Sign in", "Login form is not displayed");
    }

    @Test
    public void verifyErrorMessageForInvalidLogin() {
        loginPage.performLogin(Config.INVALID_USERNAME, Config.INVALID_PASSWORD);
        Assert.assertEquals(loginPage.getSignInMessage().trim(), LoginPage.WRONG_USERNAME_PASSWORD_MESSAGE, "Sign in message is not as expected: ");
    }
}
