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

import static utils.Config.*;

public class ProfilePage extends HeaderComponent {
    private static final String PAGE_URL = PROFILE_PAGE_URL_WITHOUT_USER_ID;

    private final WebDriverWait wait;
    private final WebDriverWait shortWait;

    @FindBy(xpath = "//div[contains(@class, 'profile-section')]//h2")
    private WebElement usernameText;
    @FindBy(xpath = "//app-post-list//app-post")
    private List<WebElement> postAppPostItem;
    @FindBy(xpath = "//i[contains(@class, 'user-edit')]")
    private WebElement editProfileIcon;
    @FindBy(xpath = "//div[contains(@class, 'profile-section')]//p")
    private WebElement publicInfoText;
    @FindBy (xpath = "//div[@class='btn-group btn-group-toggle post-filter-buttons']//label[contains(text(), 'All')]")
    private WebElement allPostsFilterButton;
    @FindBy (xpath = "//div[contains(@class, 'profile-section')]//button")
    private WebElement followUnfollowButton;

    private static final By toastMessageLocator = By.xpath
            ("//div[@class='toast-message ng-star-inserted']");

    public ProfilePage(WebDriver webDriver) {
        super(webDriver);
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        this.shortWait = new WebDriverWait(webDriver, Duration.ofSeconds(3));
        PageFactory.initElements(webDriver, this);
    }

    public boolean isUrlWithIDLoaded(int userId) {
        try {
            return wait.until(ExpectedConditions.urlToBe(PAGE_URL + userId));
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for URL to load. Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean isUsernameAsExpected(String username) {
        try {
            shortWait.until(ExpectedConditions.textToBePresentInElement(this.usernameText, username));
        } catch (TimeoutException e) {
            System.out.println("Username text is not present on profile page. Exception: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean isPublicInfoAsExpected(String infoText) {
        try {
            shortWait.until(ExpectedConditions.textToBePresentInElement(this.publicInfoText, infoText));
        } catch (TimeoutException e) {
            System.out.println("Info text is not present on profile page. Exception: " + e.getMessage());
            return false;
        }
        return true;
    }

    public int getPostCount() {
        return postAppPostItem.size();
    }

    public void clickPost(int index) {
        postAppPostItem.get(index).click();
    }

    public void clickAllPostsFilterButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(allPostsFilterButton)).click();
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for all posts filter button to be clickable. Exception: " + e.getMessage());
        }
    }

    public String getToastMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(toastMessageLocator)).getText();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for toast message to be visible. Exception: " + e.getMessage());
            return "";  // Return an empty string to indicate no message is found
        }
    }

    public void clickEditProfileIcon() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(editProfileIcon)).click();
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for edit profile icon to be clickable. Exception: " + e.getMessage());
        }
    }

    public void clickFollowUnfollowButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(followUnfollowButton)).click();
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for follow/unfollow button to be clickable. Exception: " + e.getMessage());
        }
    }

    public String getFollowUnfollowButtonCurrentText() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(followUnfollowButton)).getText();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for follow/unfollow button to be visible. Exception: " + e.getMessage());
            return "";  // Return an empty string to indicate no message is found
        }
    }

    public boolean isFollowUnfollowButtonVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(followUnfollowButton)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for follow/unfollow button to be visible. Exception: " + e.getMessage());
            return false;  // Return false to indicate the button is not visible
        }
    }
}
