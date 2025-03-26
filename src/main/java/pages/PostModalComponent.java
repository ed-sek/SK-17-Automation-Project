package pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.NoSuchElementException;

public class PostModalComponent {
    private final WebDriver webDriver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//div[@class='modal-dialog']")
    private WebElement modalElement;
    @FindBy(xpath = "//div[@class='post-modal-img']")
    private WebElement imagePostModal;
    @FindBy(xpath = "//div[@class='modal-dialog']//a[@class='post-user']")
    private WebElement postUser;
    @FindBy(xpath = "//div[@class='modal-dialog']//div[@class='post-title']")
    private WebElement postTitle;

    public PostModalComponent(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        PageFactory.initElements(webDriver, this);
    }

    public boolean isImageVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(imagePostModal)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for image element to be visible. Exception: " + e.getMessage());
            return false;
        }
    }

    public String getPostTitle() {
        try {
            return postTitle.getText();
        } catch (NoSuchElementException e) {
            System.out.println("Post title element not found. Exception: " + e.getMessage());
            return "N/A";
        }
    }

    public String getPostUser() {
        try {
            return postUser.getText();
        } catch (NoSuchElementException e) {
            System.out.println("Post user element not found. Exception: " + e.getMessage());
            return "N/A";
        }
    }
}
