package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class PostModal extends HeaderComponent {
    private final WebDriverWait wait;

    @FindBy(xpath = "//div[@class='modal-dialog']")
    private WebElement modalElement;
    @FindBy(xpath = "//div[@class='post-modal-img']")
    private WebElement imagePostModal;
    @FindBy(xpath = "//div[@class='modal-dialog']//a[@class='post-user']")
    private WebElement postUser;
    @FindBy(xpath = "//div[@class='modal-dialog']//div[@class='post-title']")
    private WebElement postTitle;
    @FindBy(xpath = "//a[text()='Delete post']")
    private WebElement deletePostLink;
    @FindBy(xpath = "//i[contains(@class, 'lock')]")
    private WebElement padlockIcon;
    @FindBy(xpath = "//input[@formcontrolname='content']")
    private WebElement commentField;

    private static By padlockUnlockedLocator = By.xpath
            ("//i[@class='fas fa-unlock ng-star-inserted']");
    private static By padlockLockedLocator = By.xpath
            ("//i[@class='fas fa-lock ng-star-inserted']");
    private static final By confirmDeleteButtonLocator = By.xpath
            ("//div[@class='delete-confirm']/button[text()='Yes']");
    private static final By cancelDeleteButtonLocator = By.xpath
            ("//div[@class='delete-confirm']/button[text()='No']");
    private static final By commentContentListLocator = By.xpath
            ("//app-comment//div[contains(@class, 'comment-content')]");

    public PostModal(WebDriver webDriver) {
        super(webDriver);
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        PageFactory.initElements(webDriver, this);
    }

    public boolean isModalVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(modalElement)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for modal element to be visible. Exception: " + e.getMessage());
            return false;
        }
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

    public void postComment(String comment) {
        this.commentField.sendKeys(comment);
        this.commentField.submit();
    }

    public boolean isCommentPresent(String expectedText) {
        List<WebElement> commentElements = webDriver.findElements(commentContentListLocator);

        for (WebElement comment : commentElements) {
            if (comment.getText().contains(expectedText)) {
                return true;
            }
        }
        return false;
    }

    public void clickDeletePost() {
        deletePostLink.click();
    }

    public void confirmDeletePost() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButtonLocator)).click();
    }

    public boolean isPadlockUnlocked() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(padlockUnlockedLocator)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for modal element to be visible. Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean isPadlockLocked() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(padlockLockedLocator)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for modal element to be visible. Exception: " + e.getMessage());
            return false;
        }
    }

    public void clickPadlock() {
        this.padlockIcon.click();
    }
}
