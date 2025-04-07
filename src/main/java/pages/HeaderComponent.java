package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HeaderComponent {

    protected final WebDriver webDriver;
    protected final WebDriverWait wait; // Default wait
    protected final WebDriverWait shortWait; // Shorter wait maybe for retries

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
    private static final By searchDropdownAnyUserLinkLocator = By.xpath("//div[@class='dropdown-container']//a[@class='post-user']");

    public HeaderComponent(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
        this.shortWait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        PageFactory.initElements(webDriver, this);
    }

    public void clickHomeIcon() {
        waitAndClick(homeIcon);
    }

    public void clickHomeLink() {
        waitAndClick(homeLink);
    }

    public void clickProfile() {
        waitAndClick(profileLink);
    }

    public void clickNewPost() {
        waitAndClick(newPostLink);
    }

    public void clickLoginLink() {
        waitAndClick(loginLink);
    }

    public void clickSignOutIcon() {
        waitAndClick(signOutIcon);
    }

    public void populateSearchField(String searchText) {
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOf(searchField));
        searchInput.clear();
        searchInput.sendKeys(searchText);

        // Wait briefly for dropdown container to become visible after typing
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(searchDropdownContainerLocator));
            System.out.println("Search dropdown appeared after typing.");
        } catch (TimeoutException e) {
            System.out.println("Search dropdown did not become visible shortly after typing, either due to delay or no results." + e.getMessage());
        }
    }

    public boolean isSearchDropdownVisible() {
        return isElementVisible(searchDropdownContainerLocator);
    }

    public void waitAndClickSearchDropdownUser(String username) {
        By specificUserLinkLocator = By.xpath("//div[@class='dropdown-container']//a[@class='post-user' and normalize-space(text())='" + username + "']");

        long startTime = System.currentTimeMillis();
        final long timeoutMillis = 20000;
        boolean clicked = false;
        String lastError = "Unknown error";

        // Retry loop
        while (!clicked && (System.currentTimeMillis() - startTime) < timeoutMillis) {
            try {
                // Ensure the dropdown container itself is present
                wait.until(ExpectedConditions.presenceOfElementLocated(searchDropdownAnyUserLinkLocator));

                // Wait for the *specific* user link to be present and then clickable
                WebElement userLink = wait.until(ExpectedConditions.elementToBeClickable(specificUserLinkLocator));

                // Click the element
                userLink.click();
                System.out.println("Successfully clicked user: " + username + " in search dropdown.");
                clicked = true; // Exit loop on success

            } catch (StaleElementReferenceException e) {
                lastError = "StaleElementReferenceException encountered. Retrying...";
                System.out.println(lastError);

            } catch (TimeoutException e) {
                lastError = "TimeoutException waiting for user '" + username + "' to be present/clickable. Retrying...";
                System.out.println(lastError);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

            } catch (ElementClickInterceptedException e) {
                lastError = "ElementClickInterceptedException clicking user '" + username + "'. Retrying...";
                System.out.println(lastError);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

            } catch (Exception e) {
                // Catch any other unexpected exceptions during the attempt
                lastError = "Unexpected exception (" + e.getClass().getSimpleName() + ") during search dropdown click attempt. Retrying...: " + e.getMessage();
                System.out.println(lastError);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        } // End of while loop

        // After the loop, check if the click was successful
        if (!clicked) {
            // Throw a clear exception if the element was never successfully clicked
            System.err.println("Final error before failing: " + lastError);
            throw new TimeoutException("Failed to find and click user '" + username + "' in search dropdown after " + timeoutMillis + "ms. Last error: " + lastError);
        }
    }

    private void waitAndClick(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (TimeoutException e) {
            System.err.println("Timeout waiting for element (" + getElementLocatorString(element) + ") to become clickable: " + e.getMessage());
            throw new TimeoutException("Element not clickable after timeout: " + getElementLocatorString(element), e);
        } catch (ElementClickInterceptedException e) {
            System.err.println("Element click intercepted for element (" + getElementLocatorString(element) + "): " + e.getMessage());

            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            try {
                System.out.println("Retrying click after interception...");
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                System.out.println("Retry click successful.");
            } catch (Exception retryEx) {
                System.err.println("Retry click also failed for element (" + getElementLocatorString(element) + "): " + retryEx.getMessage());
                throw new ElementClickInterceptedException("Element click intercepted even after retry: " + getElementLocatorString(element), retryEx);
            }
        } catch (StaleElementReferenceException e) {
            System.err.println("Stale element reference encountered during waitAndClick for element (" + getElementLocatorString(element) + "). Might need to re-find element. " + e.getMessage());
            throw e; // Re-throw stale exception
        }
    }

    private boolean isElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for element " + locator + " to be visible." + e.getMessage());
            return false;
        }
    }

    private String getElementLocatorString(WebElement element) {
        return element.toString();
    }
}

