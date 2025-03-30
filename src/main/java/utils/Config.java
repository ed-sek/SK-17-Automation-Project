package utils;

public class Config {

    // Application page URLs
    public static final String LOGIN_PAGE_URL = "http://training.skillo-bg.com:4300/users/login";
    public static final String HOME_PAGE_URL = "http://training.skillo-bg.com:4300/posts/all";
    public static final String SIGNUP_PAGE_URL = "http://training.skillo-bg.com:4300/users/register";
    public static final String POST_PAGE_URL = "http://training.skillo-bg.com:4300/posts/create";
    public static final String PROFILE_PAGE_URL_WITHOUT_USER_ID = "http://training.skillo-bg.com:4300/users/";


    // Credentials
    public static final String VALID_USERNAME_1 = "TonyStark";
    public static final String VALID_PASSWORD_1 = "IronIronIron3";
    public static final int VALID_USER_ID_1 = 9392;
    public static final String VALID_USERNAME_2 = "BruceWayne";
    public static final String VALID_PASSWORD_2 = "BatBatBat3";
    public static final int VALID_USER_ID_2 = 9391;
    public static final String VALID_USERNAME_3 = "PeterParker";
    public static final int VALID_USER_ID_3 = 9223;
    public static final String INVALID_USERNAME = "PeterPan";
    public static final String INVALID_PASSWORD = "Neverland321";

    // Directories paths
    public static final String TEST_RESOURCES_DIR = "src\\test\\resources\\";
    public static final String REPORTS_DIR = TEST_RESOURCES_DIR.concat("reports\\");
    public static final String DOWNLOAD_DIR = TEST_RESOURCES_DIR.concat("download\\");
    public static final String SCREENSHOT_DIR = TEST_RESOURCES_DIR.concat("screenshot\\");
    public static final String UPLOAD_DIR = TEST_RESOURCES_DIR.concat("upload\\");

}
