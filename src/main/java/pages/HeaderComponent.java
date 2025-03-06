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
import java.util.NoSuchElementException;
import java.util.Set;

public class HeaderComponent {
    private final WebDriver webDriver;

    private static final Set<String> VALID_MENU_OPTIONS =
            Set.of("home", "login", "profile", "homeIcon");

    @FindBy(id = "nav-link-home")
    private WebElement homeLink;
    @FindBy(id = "nav-link-login")
    private WebElement loginLink;
    @FindBy(id = "nav-link-profile")
    private WebElement profileLink;
    @FindBy(id = "homeIcon")
    private WebElement homeIcon;

    public HeaderComponent(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    // Method to locate navBarTogglerButton (sandwich) based on condition
    public WebElement getNavBarTogglerButton() {
        if (this.webDriver.manage().window().getSize().getWidth() < 768) {
            try {
                return webDriver.findElement(By.xpath("//button[@class='navbar-toggler']"));
            } catch (NoSuchElementException e) {
                System.out.println("Conditional element not found: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    public void clickHome() {
        waitAndClick(homeLink);
    }
    public void clickLogin() {
        waitAndClick(loginLink);
    }
    public void clickProfile() {
        waitAndClick(profileLink);
    }
    public void clickHomeIcon() {
        waitAndClick(homeIcon);
    }

    private void waitAndClick(WebElement element) {
        try {
            new WebDriverWait(webDriver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (TimeoutException e) {
            throw new IllegalStateException("Header navigation link not found or not clickable: " + e.getMessage());
        }
    }

    public void clickMenuLink(String menuItem) {
        String menu = menuItem.toLowerCase();

        if (!VALID_MENU_OPTIONS.contains(menu)) {
            throw new IllegalArgumentException("Invalid menu option: " + menuItem +
                    ". Allowed values: " + VALID_MENU_OPTIONS);
        }

        WebElement elementToClick;
        switch (menu) {
            case "login":
                elementToClick = loginLink;
                break;
            case "profile":
                elementToClick = profileLink;
                break;
            case "home":
                elementToClick = homeLink;
                break;
            case "homeicon":
                elementToClick = homeIcon;
                break;
            default:
                throw new IllegalStateException("Unexpected menu option: " + menu);
        }
        waitAndClick(elementToClick);
    }
}

