package pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Config;

import java.time.Duration;

public class LoginPage {
    private static final String PAGE_URL = Config.LOGIN_PAGE_URL;

    private final WebDriver webDriver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWaitForMessage;

    @FindBy(id = "defaultLoginFormUsername")
    private WebElement usernameField;
    @FindBy(id = "defaultLoginFormPassword")
    private WebElement passwordField;
    @FindBy(id = "sign-in-button")
    private WebElement signInButton;
    @FindBy(xpath = "//p[@class='h4 mb-4']")
    private WebElement signInTitle;
    @FindBy(xpath = "//*[@class='toast-message ng-star-inserted']")
    private WebElement signInMessage;

    public LoginPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        this.shortWaitForMessage = new WebDriverWait(webDriver, Duration.ofMillis(1500));
        PageFactory.initElements(webDriver, this);
    }

    public void openPage() {
        this.webDriver.get(PAGE_URL);
    }

    public boolean isUrlLoaded() {
        try {
            return wait.until(ExpectedConditions.urlToBe(PAGE_URL));
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for URL to load. Exception: " + e.getMessage());
            return false;
        }
    }

    public void populateUsername(String username) {
        this.usernameField.sendKeys(username);
    }

    public void populatePassword(String password) {
        this.passwordField.sendKeys(password);
    }

    public void clickSignIn() {
        this.signInButton.click();
    }

    public void performLogin(String username, String password) {
        this.populateUsername(username);
        this.populatePassword(password);
        this.clickSignIn();
    }

    public String getSignInFormText() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(this.signInTitle)).getText();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for sign-in title to be visible. Exception: " + e.getMessage());
            return ""; // Return an empty string to indicate no text is found on the login form
        }
    }

    public boolean isOnSignInMessagePresent(String message) {
        try {
            return shortWaitForMessage.until(ExpectedConditions.textToBePresentInElement(this.signInMessage, message));
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for sign-in message text to appear. Exception: " + e.getMessage());
            return false; // Sign-in message was not found
        }
    }

    public String getSignInMessage() {
        try {
            return shortWaitForMessage.until(ExpectedConditions.visibilityOf(this.signInMessage)).getText();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for sign-in message to be visible. Exception: " + e.getMessage());
            return "";  // Return an empty string to indicate no message is found
        }
    }
}
