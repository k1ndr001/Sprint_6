package ru.praktikum.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public abstract class BaseTest {

    protected WebDriver driver;
    private String browser;

    @BeforeEach
    void setUp() {
        browser = System.getProperty("browser", "chrome").toLowerCase(Locale.ROOT);
        driver = createDriver();
        driver.manage().window().setSize(new Dimension(1440, 900));
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private WebDriver createDriver() {
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        if ("chrome".equals(browser)) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            if (headless) {
                options.addArguments("--headless=new");
            }
            options.addArguments("--window-size=1440,900");
            return new ChromeDriver(options);
        }

        Path firefoxBinary = findFirefoxBinary();
        if (firefoxBinary == null) {
            throw new IllegalStateException(
                    "Firefox is not installed. Install Mozilla Firefox or run tests with -Dbrowser=chrome."
            );
        }

        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(firefoxBinary);
        if (headless) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    protected boolean isChrome() {
        return "chrome".equals(browser);
    }

    private Path findFirefoxBinary() {
        Path[] candidates = new Path[] {
                Paths.get("C:\\Program Files\\Mozilla Firefox\\firefox.exe"),
                Paths.get("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe")
        };

        for (Path candidate : candidates) {
            if (Files.exists(candidate)) {
                return candidate;
            }
        }

        return null;
    }
}
