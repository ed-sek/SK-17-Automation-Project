# SK-17-Automation-Project

## Overview

This project is an automation framework for testing the iSkillo web application. iSkillo is a social media platform where users can create profiles, post pictures, follow other users, and interact with content. The framework uses Selenium WebDriver and TestNG with the Page Object Model design pattern.

## Project Structure

```
├── src
│   ├── main
│   │   ├── java
│   │   │   │   ├── pages
│   │   │   │   │   ├── EditProfileModal.java
│   │   │   │   │   ├── HeaderComponent.java
│   │   │   │   │   ├── HomePage.java
│   │   │   │   │   ├── LoginPage.java
│   │   │   │   │   ├── PostModal.java
│   │   │   │   │   ├── PostPage.java
│   │   │   │   │   ├── ProfilePage.java
│   │   │   │   │   └── SignUpPage.java
│   │   │   │   └── utils
│   │   │   │       └── Config.java
│   │   │   └── resources
│   │   │       └── logback.xml
│   │   └── test
│   │       ├── java
│   │       │   └── tests
│   │       │       ├── LoginTests.java
│   │       │       ├── PostTests.java
│   │       │       ├── ProfileTests.java
│   │       │       ├── RegistrationTests.java
│   │       │       ├── TestBase.java
│   │       │       └── UserInteractionTests.java
│   │       └── resources
│   │           ├── download
│   │           ├── reports
│   │           ├── screenshot
│   │           ├── upload
│   │           │   └── testUpload-SpiderMan.jpg
│   │           ├── testng.xml
│   │           └── testng-parallel.xml
│   ├── .gitignore
│   ├── pom.xml
│   └── README.md
```

**Key Directories:**

* `src/main/java/pages`: Contains Java classes representing the page objects of the application under test.
* `src/main/java/utils`: Includes utility classes, with `Config.java` being a key file for configuration.
* `src/test/java/tests`: Contains the Java test classes.
* `src/test/resources`:  Holds test resources, including downloads, reports, screenshots, and upload files.

## How to Run the Tests

1. **Prerequisites**:
    - Java (version 23) and Maven installed.
    - Chrome browser.

2. **Setup**:
    - Clone the repository.
    - (optional) Configure any necessary settings in **Config.java** (e.g., URLs, credentials).

3. **Run Tests**:
    - Execute `mvn clean test` from the project root.
    - TestNG will run all test classes as defined in `testng.xml` in consecutive order.
    - TestNG will run all test classes as defined in `testng-parallel.xml` in parallel.

4. **Test Reports and Screenshots**:
    - Test reports, screenshots, and logs will be generated in the respective directories under `src/test/resources`.

5. **Logging**:
    - The project uses Logback for logging, configured in `src/main/resources/logback.xml`.

## Test Cases

The project includes several test classes to verify different functionalities:

* `LoginTests.java`:  Tests login functionality.
* `PostTests.java`:  Tests post-related functionalities, including creating, uploading, and deleting posts.
* `ProfileTests.java`: Tests profile-related features like editing profile information.
* `RegistrationTests.java`:  Tests user registration.
* `UserInteractionTests.java`:  Tests user interactions such as following/unfollowing users and commenting on posts.

## Key Features

- **Page Object Model (POM)**: Encapsulates UI interactions in reusable page objects.
- **Shared TestBase**: Centralizes setup and teardown logic, including WebDriver management and screenshot capture.
- **Dynamic Data Generation**: Uses unique timestamps in test data (e.g., for public info updates and registration) to ensure data uniqueness.
- **Parallel Execution**: Supports running tests in parallel for faster execution and scalability.

#### Revision: 2025-04-07
