package pages;

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

public class ProfilePage {
    private static final String PAGE_URL = PROFILE_PAGE_URL_WITHOUT_USER_ID;
    private final WebDriver webDriver;
    private final WebDriverWait wait;

    private final WebDriverWait shortWait;


    @FindBy(xpath = "//div[contains(@class, 'profile-section')]//h2")
    private WebElement usernameText;
    @FindBy(xpath = "//app-post-list//app-post")
    private List<WebElement> postAppPostItem;

    public ProfilePage(WebDriver webDriver) {
        this.webDriver = webDriver;
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

    public int getPostCount() {
        return postAppPostItem.size();
    }

    public void clickPost(int index) {
        postAppPostItem.get(index).click();
    }
}
