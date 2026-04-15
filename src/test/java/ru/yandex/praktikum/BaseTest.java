package ru.yandex.praktikum;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;  // ✅ ДОБАВЛЕНО: импорт для By
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";

    @Before
    public void setUp() {
        String browser = "chrome";

        if (browser.equals("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-blink-features=AutomationControlled");
            driver = new ChromeDriver(options);
        } else {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            driver = new FirefoxDriver(options);
        }

        driver.manage().window().maximize();
        driver.get(BASE_URL);

        // ✅ Ждём полной загрузки страницы
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        // ✅ Ждём, пока исчезнет loader (если есть)
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".loader, .loading, [class*='loader'], [class*='loading']")
            ));
        } catch (Exception e) {
            // Игнорируем, если loader не найден
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}