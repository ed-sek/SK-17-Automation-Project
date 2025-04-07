package pages;

import org.openqa.selenium.*;
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
    private final WebDriverWait longWait; // Added for potentially longer operations
    private final WebDriverWait shortWait;

    @FindBy(xpath = "//div[contains(@class, 'profile-section')]//h2")
    private WebElement usernameText;
    @FindBy(xpath = "//i[contains(@class, 'user-edit')]")
    private WebElement editProfileIcon;
    @FindBy(xpath = "//div[contains(@class, 'profile-section')]//p")
    private WebElement publicInfoText;
    @FindBy(xpath = "//div[@class='btn-group btn-group-toggle post-filter-buttons']//label[contains(text(), 'All')]")
    private WebElement allPostsFilterButton;
    @FindBy(xpath = "//div[contains(@class, 'profile-section')]//button")
    private WebElement followUnfollowButton;

    private static final By followUnfollowButtonLocator = By.xpath
            ("//div[contains(@class, 'profile-section')]//button");
    private static final By toastMessageLocator = By.xpath
            ("//div[@class='toast-message ng-star-inserted']");
    private static final By appPostListLocator = By.xpath
            ("//app-post-list");
    private static final By postAppPostItemLocator = By.xpath
            ("//app-post-list//app-post");
    private static final By loaderElementLocator = By.xpath
            ("//div[@class='loader']");

    public ProfilePage(WebDriver webDriver) {
        super(webDriver);
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
        this.longWait = new WebDriverWait(webDriver, Duration.ofSeconds(30));
        this.shortWait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        PageFactory.initElements(webDriver, this);
    }

    public boolean isUrlWithIDLoaded(int userId) {
        try {
            return wait.until(ExpectedConditions.urlToBe(PAGE_URL + userId));
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for Profile URL (" + PAGE_URL + userId + ") to load. Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean isAppPostListLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(appPostListLocator)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for App post list container to be displayed. Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean isUsernameAsExpected(String username) {
        try {
            return shortWait.until(ExpectedConditions.textToBePresentInElement(this.usernameText, username));
        } catch (TimeoutException e) {
            System.out.println("Username text '" + username + "' is not present or does not match on profile page. Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean isPublicInfoAsExpected(String infoText) {
        try {
            return shortWait.until(ExpectedConditions.textToBePresentInElement(this.publicInfoText, infoText));
        } catch (TimeoutException e) {
            System.out.println("Public info text '" + infoText + "' is not present or does not match on profile page. Exception: " + e.getMessage());
            return false;
        }
    }

    public int getPostCount() {
        // Ensure the list container is visible before trying to count
        waitForAppPostListContainerVisible();

        // Find elements dynamically each time count is requested
        List<WebElement> postElements = webDriver.findElements(postAppPostItemLocator);
        return postElements.size();
    }

    private void waitForAppPostListContainerVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(appPostListLocator));
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for the app post list container to become visible. Exception: " + e.getMessage());

        }
    }

    private void waitForPostElementsToBePresent() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(appPostListLocator));

            wait.until(ExpectedConditions.presenceOfElementLocated(postAppPostItemLocator));
        } catch (TimeoutException e) {
            // If timeout occurs, check if the container is visible (meaning 0 posts is likely)
            try {
                if (webDriver.findElement(appPostListLocator).isDisplayed()) {
                    System.out.println("Post list container is visible, but no post elements found (potentially 0 posts).");
                } else {
                    System.out.println("Neither post container nor post elements found after timeout.");
                }
            } catch (NoSuchElementException nse) {
                System.out.println("Post list container itself not found after initial timeout." + nse.getMessage());
            }
        }
    }

    public void clickPost(int index) {
        waitForPostElementsToBePresent();
        List<WebElement> postAppPostItem = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(postAppPostItemLocator));

        if (index >= 0 && index < postAppPostItem.size()) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(postAppPostItem.get(index))).click();
            } catch (TimeoutException e) {
                System.out.println("Timeout waiting for post at index " + index + " to be clickable. Exception: " + e.getMessage());
                throw e; // Re-throw exception to fail the test
            } catch (ElementClickInterceptedException e) {
                System.out.println("Click intercepted for post at index " + index + ". Exception: " + e.getMessage());
                throw e; // Re-throw exception
            }
        } else {
            throw new IndexOutOfBoundsException("Post index " + index + " is out of bounds for post list size " + postAppPostItem.size());
        }
    }

    public void clickAllPostsFilterButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(allPostsFilterButton)).click();
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for 'All Posts' filter button to be clickable. Exception: " + e.getMessage());
            throw new TimeoutException("All Posts filter button not clickable.", e);
        }
    }

    public String getToastMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(toastMessageLocator)).getText();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for toast message to be visible. Exception: " + e.getMessage());
            return "";  // Return an empty string to indicate no message found
        }
    }

    public void clickEditProfileIcon() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(editProfileIcon)).click();
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for edit profile icon to be clickable. Exception: " + e.getMessage());
            throw new TimeoutException("Edit Profile icon not clickable.", e);
        }
    }

    // Method to just click the button (no wait for text change)
    public void clickFollowUnfollowButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(followUnfollowButton)).click();
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for follow/unfollow button to be clickable. Exception: " + e.getMessage());
            throw new TimeoutException("Follow/Unfollow button not clickable.", e);
        }
    }

    public boolean clickFollowUnfollowAndWaitForTextChange(String expectedTextAfterClick) {
        try {
            // Wait for the button to be clickable using the WebElement field
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(followUnfollowButton));
            String initialText = button.getText(); // Get text before click for comparison later if needed
            System.out.println("Button text before click: " + initialText);
            button.click();

            return wait.until(ExpectedConditions.textToBe(followUnfollowButtonLocator, expectedTextAfterClick));
        } catch (TimeoutException e) {
            System.out.println("Timeout while clicking or waiting for Follow/Unfollow button text to change to: " + expectedTextAfterClick + ". Exception: " + e.getMessage());
            try {
                // Use the By locator to re-find the element reliably after timeout
                System.out.println("Current button text after timeout: " + webDriver.findElement(followUnfollowButtonLocator).getText());
            } catch (Exception innerEx) {
                System.out.println("Could not retrieve current button text after timeout. " + innerEx.getMessage());
            }
            return false;
        } catch (StaleElementReferenceException e) {
            System.out.println("StaleElementReferenceException during follow/unfollow click/wait. Exception: " + e.getMessage());
            return false; // Indicate failure
        }
    }

    public String getFollowUnfollowButtonCurrentText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(followUnfollowButtonLocator)).getText();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for follow/unfollow button to be visible to get text. Exception: " + e.getMessage());
            return "";  // Return an empty string if not found/visible
        }
    }

    public boolean isFollowUnfollowButtonVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(followUnfollowButtonLocator)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for follow/unfollow button to be visible. Exception: " + e.getMessage());
            return false;
        }
    }

    public void waitForLoaderToDisappear() {
        try {
            longWait.until(ExpectedConditions.invisibilityOfElementLocated(loaderElementLocator));
            System.out.println("The loader has disappeared.");
        } catch (TimeoutException e) {
            System.out.println("Timed out waiting for the loader to disappear. Continuing execution...");
        }
    }

    public boolean waitForPostCountToBe(int expectedCount) {
        try {
            waitForAppPostListContainerVisible();

            longWait.until(ExpectedConditions.numberOfElementsToBe(postAppPostItemLocator, expectedCount));
            return true;
        } catch (TimeoutException e) {
            System.out.println("Timed out waiting for post count to become: " + expectedCount + ". Exception: " + e.getMessage());
            // Log the actual count found after the timeout for debugging
            System.out.println("Actual post count found after timeout: " + webDriver.findElements(postAppPostItemLocator).size());
            return false;
        }
    }

    public boolean waitForPostCountToBeGreaterThan(int count) {
        try {
            waitForAppPostListContainerVisible();

            longWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(postAppPostItemLocator, count));
            return true;
        } catch (TimeoutException e) {
            System.out.println("Timed out waiting for post count to be greater than: " + count + ". Exception: " + e.getMessage());
            System.out.println("Actual post count found after timeout: " + webDriver.findElements(postAppPostItemLocator).size());
            return false;
        }
    }


}