package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static utils.Config.*;

public abstract class TestBase {

    private WebDriver webDriver;

    @BeforeSuite
    protected void setupTestSuite() {
        cleanDirectory(REPORTS_DIR);
        cleanDirectory(SCREENSHOT_DIR);
        WebDriverManager.chromedriver().setup();
        //Future-proof: Uncomment the following lines to set up other browsers
        //WebDriverManager.firefoxdriver().setup();
        //WebDriverManager.edgedriver().setup();
    }

    @BeforeMethod
    protected void setUpTest() {
        this.webDriver = new ChromeDriver(configChromeOptions());
        this.webDriver.manage().window().maximize();
        this.webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        this.webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @AfterMethod
    protected void tearDownTest(ITestResult testResult) {
        takeScreenshot(testResult);
        quitDriver();
    }

    @AfterSuite
    protected void deleteDownloadedFiles() {
        cleanDirectory(DOWNLOAD_DIR);
    }

    protected WebDriver getDriver() {
        return this.webDriver;
    }

    private void quitDriver() {
        if (this.webDriver != null) {
            try {
                this.webDriver.quit();
            } catch (Exception e) {
                System.out.println("Error during WebDriver cleanup. Exception: " + e.getMessage());
            }
        }
    }

    private void cleanDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        Assert.assertTrue(directory.isDirectory(), "Invalid directory.");

        try {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();

                    // Skipping files we want to keep
                    if (fileName.equals(".gitkeep") || fileName.equals("testUpload-SpiderMan.jpg")) {
                        continue;
                    }

                    FileUtils.forceDelete(file);
                }

                System.out.printf("[STATUS] Cleaned directory (excluding .gitkeep): %s%n%n", directoryPath);
            }
        } catch (IOException e) {
            System.out.println("Error during directory cleaning. Exception: " + e.getMessage());
        }
    }

    // Sets custom download directory and skips the download confirmation in ChromeOptions
    private ChromeOptions configChromeOptions() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", System.getProperty("user.dir").concat("\\").concat(DOWNLOAD_DIR));
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", prefs);
        return chromeOptions;
    }

    private void takeScreenshot(ITestResult testResult) {
        if (ITestResult.FAILURE == testResult.getStatus()) {
            try {
                // Cast webDriver to TakesScreenshot to take a screenshot
                TakesScreenshot takesScreenshot = (TakesScreenshot) webDriver;
                File screenshot = takesScreenshot.getScreenshotAs(OutputType.FILE);
                String testName = testResult.getName();

                // Append any test parameters to the test name (if they are not null or empty)
                for (Object param : testResult.getParameters()) {
                    if (param != null && !param.toString().isEmpty()) {
                        testName = testName + "_" + param;
                    }
                }

                // Get the current timestamp to make the filename unique
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                String timestamp = LocalDateTime.now().format(formatter);

                // Combine the test name and timestamp to create a unique filename
                String fileName = testName + "_" + timestamp + ".jpg";

                // Save the screenshot to the specified directory with the timestamped test name
                FileUtils.copyFile(screenshot, new File(SCREENSHOT_DIR.concat(fileName)));

            } catch (IOException e) {
                System.out.println("Error while taking screenshot. Exception: " + e.getMessage());
            }
        }
    }
}
