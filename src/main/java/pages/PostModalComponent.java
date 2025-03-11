package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.NoSuchElementException;

public class PostModalComponent {
    private final WebDriver webDriver;
    private final WebDriverWait wait;
    private final WebElement modalElement;

    // TODO: Implement PageFactory locators here for the elements

    public PostModalComponent(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        this.modalElement = webDriver.findElement(By.className("post-modal"));
    }

    public boolean isImageVisible() {
        try {
            WebElement image = modalElement.findElement(By.xpath(".//div[@class='post-modal-img']/img"));
            return wait.until(ExpectedConditions.visibilityOf(image)).isDisplayed();
        } catch (NoSuchElementException e) {
            System.out.println("The image is not visible: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getPostTitle() {
        WebElement postTitle = modalElement.findElement(By.xpath(".//div[@class='post-title']"));
        return postTitle.getText();
    }

    public String getPostUser() {
        WebElement postUser = modalElement.findElement(By.xpath(".//a[@class='post-user']"));
        return postUser.getText();
    }
}
