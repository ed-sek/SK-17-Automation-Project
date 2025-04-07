package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static utils.Config.*;

public class HomePage extends HeaderComponent {
    public static final String PAGE_URL = HOME_PAGE_URL;

    private final WebDriverWait wait;

    private static final By toastMessageLocator = By.xpath
            ("//div[@class='toast-message ng-star-inserted']");

    public HomePage(WebDriver webDriver) {
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

    public void performLogOut() {
        clickSignOutIcon();
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
