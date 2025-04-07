package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditProfileModal extends HeaderComponent {

    @FindBy(xpath = "//app-edit-profile-modal")
    private WebElement editProfileModal;
    @FindBy(xpath = "//input[@formcontrolname='username']")
    private WebElement usernameField;
    @FindBy(xpath = "//input[@formcontrolname='email']")
    private WebElement emailField;
    @FindBy(xpath = "//input[@formcontrolname='password']")
    private WebElement passwordField;
    @FindBy(xpath = " //input[@formcontrolname='confirmPassword']")
    private WebElement confirmPasswordField;
    @FindBy(xpath = "//textarea[@formcontrolname='publicInfo']")
    private WebElement publicInfoArea;
    @FindBy(xpath = "//button[@class='btn btn-primary']")
    private WebElement saveButton;

    public EditProfileModal(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    public boolean isModalDisplayed() {
        return editProfileModal.isDisplayed();
    }

    public void fillPublicInfo(String publicInfo) {
        publicInfoArea.clear();
        publicInfoArea.sendKeys(publicInfo);
    }

    public void clickSaveButton() {
        saveButton.click();
    }
}
