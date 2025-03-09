package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public abstract class TestBase {

    public static final String TEST_RESOURCES_DIR = "src\\test\\resources";
    public static final String REPORTS_DIR = TEST_RESOURCES_DIR.concat("\\reports");

    private WebDriver webDriver;

    @BeforeSuite
    protected void setupTestSuite() {
        cleanDirectory(REPORTS_DIR);
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

    private void cleanDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        Assert.assertTrue(directory.isDirectory(), "Invalid directory.");

        try {
            FileUtils.cleanDirectory(directory);

            String[] fileList = directory.list();
            if (fileList != null && fileList.length == 0) {
                System.out.printf("All files are deleted in directory: %s%n%n", directory);
            } else {
                System.out.printf("Unable to delete the files in directory: %s%n%n", directory);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while cleaning the directory: " + e.getMessage());
        }
    }
}
