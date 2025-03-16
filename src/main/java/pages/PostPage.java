package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Config;

import java.io.File;
import java.time.Duration;
import java.util.NoSuchElementException;

public class PostPage {
    public static final String PAGE_URL = Config.POST_PAGE_URL;

    private final WebDriver webDriver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//input[@class='form-control input-lg']")
    private WebElement imageNameField;
    @FindBy(xpath = "//input[contains(@class, 'file') and @type='file']")
    private WebElement uploadField;
    @FindBy(name = "caption")
    private WebElement captionField;
    @FindBy(id = "create-post")
    private WebElement createPostButton;

    public PostPage(WebDriver webDriver) {
        this.webDriver = webDriver;
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
            WebElement image = webDriver.findElement(By.xpath("//img[@class='image-preview']"));
            return wait.until(ExpectedConditions.visibilityOf(image)).isDisplayed();
        } catch (NoSuchElementException e) {
            System.out.println("Image element not found. Exception: " + e.getMessage());
            return false;
        }
    }

    public String getImageName() {
        return imageNameField.getDomAttribute("placeholder");
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
