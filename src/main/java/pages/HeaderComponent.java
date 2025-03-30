package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class HeaderComponent {
    protected final WebDriver webDriver;
    protected final WebDriverWait wait;

    @FindBy(id = "homeIcon")
    private WebElement homeIcon;
    @FindBy(id = "nav-link-home")
    private WebElement homeLink;
    @FindBy(id = "nav-link-profile")
    private WebElement profileLink;
    @FindBy(id = "nav-link-new-post")
    private WebElement newPostLink;
    @FindBy(id = "nav-link-login")
    private WebElement loginLink;
    @FindBy(xpath = "//i[@class='fas fa-sign-out-alt fa-lg']")
    private WebElement signOutIcon;
    @FindBy(xpath = "//input[@id='search-bar']")
    private WebElement searchField;

    private static final By searchDropdownContainerLocator = By.xpath("//div[@class='dropdown-container']");
    private static final By searchDropdownItemLocator = By.xpath("//div[@class='dropdown-container']//app-small-user-profile");
    private static final By searchDropdownFollowButtonLocator = By.xpath(".//button");
    private static final By searchDropdownUserNameElementLocator = By.xpath(".//a[@class='post-user']");

    public HeaderComponent(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        PageFactory.initElements(webDriver, this);
    }

    public void clickProfile() {
        waitAndClick(profileLink);
    }


    public void clickNewPost() {
        waitAndClick(newPostLink);
    }

    public void clickSignOutIcon() {
        waitAndClick(signOutIcon);
    }


    public void populateSearchField(String searchText) {
        searchField.sendKeys(searchText);
    }


    public boolean isSearchDropdownVisible() {
        return isElementVisible(searchDropdownContainerLocator);
    }

    public WebElement findSearchDropdownItem(String searchText) {
        if (!isSearchDropdownVisible())
            throw new NoSuchElementException("Search dropdown is not visible.");

        List<WebElement> items = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(searchDropdownItemLocator));

        for (WebElement item : items) {
            try {
                WebElement userNameElement = item.findElement(searchDropdownUserNameElementLocator);
                if (userNameElement.getText().contains(searchText))
                    return item;
            } catch (NoSuchElementException ignored) {
                // Continue if an item doesn't contain the expected sub-element
            }
        }
        return null; // Return null if no matching item is found
    }

    public void waitAndClickSearchDropdownUser(String username) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        final By searchDropdownUserLocator = By.xpath("//div[@class='dropdown-container']//a[@class='post-user']"); // Local scope

        long startTime = System.currentTimeMillis();
        final long timeout = 5000; // Maximum wait time in milliseconds
        boolean clicked = false;

        while (!clicked && (System.currentTimeMillis() - startTime) < timeout) {
            try {
                List<WebElement> searchResults = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(searchDropdownUserLocator));
                for (WebElement result : searchResults) {
                    if (result.getText().trim().equals(username)) {
                        wait.until(ExpectedConditions.elementToBeClickable(result)).click();
                        clicked = true;
                        break;
                    }
                }
                if (!clicked) {
                    Thread.sleep(500); // Wait before retrying
                }
            } catch (StaleElementReferenceException | InterruptedException e) {
                // Log the exception, but don't fail immediately. Retry.
                System.out.println("StaleElementReferenceException caught, retrying...: " + e.getMessage());
                Thread.sleep(500);
            }
        }

        if (!clicked) {
            throw new NoSuchElementException("User with username: " + username + " was not found or clickable in the search dropdown after " + timeout + "ms!");
        }
    }

    private void waitAndClick(WebElement element) {
        try {
            new WebDriverWait(webDriver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for element to become clickable: " + e.getMessage());

            // Throwing IllegalStateException to halt execution when element isn't clickable, since TestNG asserts have 'test' scope
            throw new IllegalStateException("Exception during element click operation. Exception: " + e.getMessage());
        }

    }

    private boolean isElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for element to be visible. Exception: " + e.getMessage());
            return false;
        }
    }

}

