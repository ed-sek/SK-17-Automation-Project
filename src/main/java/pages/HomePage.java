package pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Config;

import java.time.Duration;

public class HomePage {
    public static final String PAGE_URL = Config.HOME_PAGE_URL;

    private final WebDriver webDriver;

    //TODO add pagefactory locators here

    public HomePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void openPage() {
        this.webDriver.get(PAGE_URL);
    }

    public boolean isUrlLoaded() {
        WebDriverWait explicitWait = new WebDriverWait(this.webDriver, Duration.ofSeconds(10));
        try {
            explicitWait.until(ExpectedConditions.urlToBe(PAGE_URL));
        } catch (TimeoutException ex) {
            return false;
        }
        return true;
    }

}
