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

import static utils.Config.*;

public class SignUpPage extends HeaderComponent {
    public static final String PAGE_URL = SIGNUP_PAGE_URL;

    private final WebDriverWait wait;

    @FindBy(xpath = "//h4[@class='text-center mb-4']")
    private WebElement signUpTitle;
    @FindBy(xpath = "//input[@formcontrolname='username']")
    private WebElement usernameField;
    @FindBy(xpath = "//input[@formcontrolname='email']")
    private WebElement emailField;
    @FindBy(xpath = "//input[@formcontrolname='birthDate']")
    private WebElement birthDateField;
    @FindBy(xpath = "//input[@formcontrolname='password']")
    private WebElement passwordField;
    @FindBy(xpath = "//input[@formcontrolname='confirmPassword']")
    private WebElement confirmPasswordField;
    @FindBy(xpath = "//textarea[@formcontrolname='publicInfo']")
    private WebElement publicInfoField;
    @FindBy(xpath = "//button[@id='sign-in-button']")
    private WebElement signInButton;

    private static final By toastMessageLocator = By.xpath
            ("//div[@class='toast-message ng-star-inserted']");

    public SignUpPage(WebDriver webDriver) {
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
            System.out.println("Timeout while waiting for URL to load. Exception: " + e.getMessage());
            return false;
        }
    }

    public void populateUsername(String username) {
        this.usernameField.sendKeys(username);
    }

    public void populateEmail(String email) {
        this.emailField.sendKeys(email);
    }

    public void populatePassword(String password) {
        this.passwordField.sendKeys(password);
    }

    public void populateConfirmPassword(String confirmPassword) {
        this.confirmPasswordField.sendKeys(confirmPassword);
    }

    public void populatePublicInfo(String publicInfo) {
        this.publicInfoField.sendKeys(publicInfo);
    }

    public void populateBirthDate(String birthDate) {
        this.birthDateField.sendKeys(birthDate);
    }

    public void clickSignIn() {
        this.signInButton.click();
    }

    public String getToastMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(toastMessageLocator)).getText();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for toast message to be visible. Exception: " + e.getMessage());
            return "";  // Return an empty string to indicate no message is found
        }
    }
}
