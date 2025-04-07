package pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.NoSuchElementException;

import static utils.Config.*;

public class PostPage extends HeaderComponent {
    public static final String PAGE_URL = POST_PAGE_URL;

    private final WebDriverWait wait;

    @FindBy(xpath = "//img[@class='image-preview']")
    private WebElement imagePreview;
    @FindBy(xpath = "//input[@class='form-control input-lg']")
    private WebElement uploadImageNameField;
    @FindBy(xpath = "//input[contains(@class, 'file') and @type='file']")
    private WebElement uploadField;
    @FindBy(xpath = "//input[@name='caption']")
    private WebElement captionField;
    @FindBy(xpath = "//button[@id='create-post']")
    private WebElement createPostButton;

    public PostPage(WebDriver webDriver) {
        super(webDriver);
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        PageFactory.initElements(webDriver, this);
    }

    public void openPage() {
        this.webDriver.get(PAGE_URL);
    }

    public boolean isUrlLoaded() {
        try {
            return wait.until(ExpectedConditions.urlToBe(PAGE_URL));
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for URL to load. Exception:  " + e.getMessage());
            return false;
        }
    }

    public boolean isImageVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(imagePreview)).isDisplayed();
        } catch (NoSuchElementException e) {
            System.out.println("Image element not found. Exception: " + e.getMessage());
            return false;
        } catch (TimeoutException e) {
            System.out.println("Image found but not visible within timeout. Exception: " + e.getMessage());
            return false;
        }
    }

    public String getImageName() {
        return uploadImageNameField.getDomAttribute("placeholder");
    }

    public void uploadPicture(File file) {
        this.uploadField.sendKeys(file.getAbsolutePath());
    }

    public void populatePostCaption(String caption) {
        this.captionField.sendKeys(caption);
    }

    public void clickCreatePost() {
        this.createPostButton.click();
    }
}
