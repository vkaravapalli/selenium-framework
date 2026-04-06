# Selenium Automation Framework

Java + Selenium 4 + TestNG + Page Object Model + ExtentReports

## Stack
| Tool | Version |
|------|---------|
| Java | 11+ |
| Selenium | 4.18.1 |
| TestNG | 7.9.0 |
| ExtentReports | 5.1.1 |
| WebDriverManager | 5.8.0 |
| Maven | 3.x |

## Project Structure
```
selenium-framework/
├── pom.xml
├── src/
│   ├── main/java/com/framework/
│   │   ├── base/BasePage.java          ← all page objects extend this
│   │   ├── config/ConfigReader.java    ← reads config.properties
│   │   ├── driver/DriverManager.java   ← ThreadLocal WebDriver
│   │   └── utils/
│   │       ├── ExtentReportManager.java
│   │       └── WaitUtils.java
│   └── test/
│       ├── java/com/framework/
│       │   ├── base/BaseTest.java      ← all tests extend this
│       │   ├── pages/LoginPage.java    ← example page object
│       │   └── tests/LoginTest.java    ← example tests
│       └── resources/
│           ├── config.properties
│           ├── testng.xml
│           └── log4j2.xml
└── reports/                            ← generated at runtime
    ├── ExtentReport.html
    ├── automation.log
    └── screenshots/
```

## Run Tests

```bash
# Default (Chrome)
mvn clean test

# Firefox
mvn clean test -Dbrowser=firefox

# Edge
mvn clean test -Dbrowser=edge
```

## Adding a New Page

1. Create `src/test/java/com/framework/pages/YourPage.java`
2. Extend `BasePage`
3. Use `@FindBy` for locators
4. Use inherited methods: `click()`, `sendKeys()`, `getText()`, `isDisplayed()`

## Adding a New Test

1. Create `src/test/java/com/framework/tests/YourTest.java`
2. Extend `BaseTest`
3. Use `getDriver()` to get the current WebDriver
4. Use `ExtentReportManager.logInfo/logPass/logFail()` for reporting
5. Add your class to `testng.xml`
