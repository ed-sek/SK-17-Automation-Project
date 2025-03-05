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
    private HomePage homePage;

    @BeforeSuite
    protected final void setUpTestSuite() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    protected final void setupTest() {
        this.webDriver = new ChromeDriver();
        this.webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        this.webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        this.webDriver.manage().window().maximize();

        this.loginPage = new LoginPage(this.webDriver);
        this.homePage = new HomePage(this.webDriver);

        this.webDriver.navigate().to(Config.LOGIN_PAGE_URL);
    }

    @AfterSuite
    public void tearDownTestSuite() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    public void verifyLoginPageURLIsLoaded() {
        Assert.assertTrue(loginPage.isUrlLoaded(), "The Login page is not loaded.");
    }

    @Test
    public void verifySignInFormIsDisplayed() {
        String expectedFormText = "Sign in";
        Assert.assertEquals(loginPage.getSignInFormText(), expectedFormText, "Login form is not displayed");
    }

    @Test
    public void verifyErrorMessageForInvalidLogin() {
        loginPage.performLogin(Config.INVALID_USERNAME, Config.INVALID_PASSWORD);

        String expectedMessage = "Wrong username or password!";
        Assert.assertEquals(loginPage.getSignInMessage().trim(), expectedMessage, "Sign in message is not as expected.");
    }

    @Test
    public void loginWithValidCredentials() {
        loginPage.performLogin(Config.VALID_USERNAME, Config.VALID_PASSWORD);

        String expectedMessage = "Successful login!";
        Assert.assertEquals(loginPage.getSignInMessage().trim(), expectedMessage, "Sign in message is not as expected.");

        Assert.assertTrue(homePage.isUrlLoaded(), "Home page is not loaded.");
    }
}
