package pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static utils.Config.*;

import java.time.Duration;

public class SignUpPage {
    public static final String PAGE_URL = SIGNUP_PAGE_URL;

    private final WebDriver webDriver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//h4[@class='text-center mb-4']")
    private WebElement signUpTitle;
    @FindBy(xpath = "//input[@formcontrolname='username']")
    private WebElement usernameField;
    @FindBy(xpath = "//input[@formcontrolname='email']")
    private WebElement emailField;
    // TODO: implement improved locator for birthDate picker
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

    public SignUpPage(WebDriver webDriver) {
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

    public String getSignUpFormText() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(this.signUpTitle)).getText();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for sign-up title to be visible. Exception: " + e.getMessage());
            return "";
        }
    }
}
