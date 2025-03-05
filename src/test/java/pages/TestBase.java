package pages;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

public abstract class TestBase {

    private WebDriver webDriver;

    @BeforeSuite
    protected void setupTestSuite() {
        WebDriverManager.chromedriver().setup();
        WebDriverManager.firefoxdriver().setup();
        WebDriverManager.edgedriver().setup();
    }

    @BeforeMethod
    protected void setUpTest() {
        this.webDriver = new ChromeDriver();
        this.webDriver.manage().window().maximize();
        this.webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        this.webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @AfterMethod
    protected void tearDownTest() {
        quitDriver();
    }

    protected WebDriver getDriver() {
        return this.webDriver;
    }

    private void quitDriver() {
        if (this.webDriver != null) {
            try {
                this.webDriver.quit();
            } catch (Exception e) {
                System.out.println("[ERROR] Error during WebDriver cleanup: " + e.getMessage());
            }
        }
    }
}
