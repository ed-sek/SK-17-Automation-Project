package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PostModal extends HeaderComponent {

    private final WebDriverWait wait;

    @FindBy(xpath = "//app-post-modal")
    private WebElement modalDialog;
    @FindBy(xpath = "//div[contains(@class, 'post-modal-container')]//img")
    private WebElement modalImage;
    @FindBy(xpath = "//div[contains(@class, 'modal-content')]//h4")
    private WebElement postTitle;
    @FindBy(xpath = "//div[contains(@class, 'modal-content')]//i[@class='like far fa-heart fa-2x ng-star-inserted']")
    private WebElement postLikeButton;
    @FindBy(xpath = "//div[contains(@class, 'modal-content')]//i[@class='like fas fa-heart fa-2x ng-star-inserted']")
    private WebElement postLikedButton;
    @FindBy(xpath = "//i[contains(@class, 'fa-unlock')]")
    private WebElement padlockOpenIcon;
    @FindBy(xpath = "//div[contains(@class, 'modal-content')]//i[contains(@class, 'fa-lock')]")
    private WebElement padlockClosedIcon;
    @FindBy(xpath = "//i[contains(@class, 'lock')]")
    private WebElement padlockIcon;
    @FindBy(xpath = "//div[contains(@class, 'modal-content')]//a[contains(@class, 'post-user')]")
    private WebElement postUserLink;
    @FindBy(xpath = "//div[contains(@class,'modal-content')]//i[contains(@class,'fa-trash-alt')]")
    private WebElement deletePostButton;

    // Locators for Comments
    @FindBy(xpath = "//app-comment-form//input[@formcontrolname='content']")
    private WebElement commentInputField;
    @FindBy(xpath = "//app-comment-form//button[text()='Send']")
    private WebElement sendCommentButton;

    private static final By confirmDeleteButtonLocator = By.xpath
            ("//div[@class='delete-confirm']/button[text()='Yes']");
    private static final By padlockLockedLocator = By.xpath
            ("//i[@class='fas fa-lock ng-star-inserted']");

    // Locator for finding a specific comment text within the modal
    private By getCommentByTextLocator(String commentText) {
        return By.xpath("//app-comment//div[contains(@class, 'comment-content') and contains(text(), '" + commentText + "')]");
    }

    public PostModal(WebDriver webDriver) {
        super(webDriver);
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
        PageFactory.initElements(webDriver, this);
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public boolean isModalVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(modalDialog)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Post modal dialog did not become visible. Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean isImageVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(modalImage)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Modal image did not become visible. Exception: " + e.getMessage());
            return false;
        }
    }

    public String getPostTitle() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(postTitle)).getText();
        } catch (TimeoutException e) {
            System.out.println("Post title did not become visible. Exception: " + e.getMessage());
            return "";
        }
    }

    public String getPostUser() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(postUserLink)).getText();
        } catch (TimeoutException e) {
            System.out.println("Post user link did not become visible. Exception: " + e.getMessage());
            return "";
        }
    }

    public boolean isPadlockLocked() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(padlockLockedLocator)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for padlock-locked element to be visible. Exception: " + e.getMessage());
            return false;
        }
    }

    public void clickPadlock() {
        this.padlockIcon.click();
    }

    // Delete Post Methods
    public void clickDeletePost() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(deletePostButton)).click();
        } catch (TimeoutException e) {
            System.out.println("Timeout clicking the delete post button. Exception: " + e.getMessage());
        }
    }

    public void confirmDeletePost() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButtonLocator)).click();
        } catch (TimeoutException e) {
            System.out.println("Timeout clicking the confirm delete button. Exception: " + e.getMessage());
        }
    }

    public void postComment(String commentText) {
        try {
            WebElement inputField = wait.until(ExpectedConditions.visibilityOf(commentInputField));
            inputField.sendKeys(commentText);
            inputField.submit();
        } catch (TimeoutException e) {
            System.out.println("Timeout interacting with comment form elements. Exception: " + e.getMessage());
        }
    }

    public boolean waitForCommentPresent(String commentText) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(getCommentByTextLocator(commentText)));
            return true;
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for comment '" + commentText + "' to appear. Exception: " + e.getMessage());
            return false;
        }
    }
}
