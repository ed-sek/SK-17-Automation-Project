package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;

public class DownloadTests extends TestBase {

    // TODO: Refactor once the actual Page Object is implemented; currently a placeholder for logic
    @Test
    public void testDownload() {
        WebDriver webDriver = getDriver();
        webDriver.navigate().to("https://demoqa.com/upload-download");
        WebElement downloadButton = webDriver.findElement(By.id("downloadButton"));

        // Scroll the element into view
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", downloadButton);

        // Wait for the download button to be clickable
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        downloadButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("downloadButton")));

        downloadButton.click();

        // The filename value comes from the website; adjust as necessary if the filename changes
        String fileName = "sampleFile.jpeg";
        File file = new File(DOWNLOAD_DIR.concat(fileName));
        Assert.assertTrue(isFileDownloaded(file), "The file is not downloaded!");
    }

    // TODO: Consider moving isFileDownloaded method to TestBase for reusability across multiple tests
    private boolean isFileDownloaded(File file) {
        int waitTime = 20;
        int counter = 0;

        try {
            while (counter < waitTime) {
                if (file.exists()) {
                    return true;
                }
                Thread.sleep(1000);
                counter++;
            }
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted while waiting for file download:" + e.getMessage());
        }

        return false;
    }
}
