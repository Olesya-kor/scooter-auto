package ru.yandex.praktikum.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class OrderFormPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы - шаг 1
    private final By nameInput = By.cssSelector("input[placeholder*='Имя']");
    private final By surnameInput = By.cssSelector("input[placeholder*='Фамилия']");
    private final By addressInput = By.cssSelector("input[placeholder*='Адрес']");
    private final By metroInput = By.cssSelector("input[placeholder*='Метро']");
    private final By phoneInput = By.cssSelector("input[placeholder*='Телефон']");
    private final By nextButton = By.xpath("//button[contains(text(), 'Далее')]");
    private final By cookieBanner = By.className("App_CookieConsent__1yUIN");

    // Шаг 2
    private final By dateInput = By.cssSelector("input[placeholder*='Когда']");
    private final By periodDropdown = By.name("period");
    private final By colorBlack = By.id("black");
    private final By colorGrey = By.id("grey");
    private final By commentInput = By.cssSelector("textarea[placeholder*='Комментарий']");
    private final By orderButton = By.xpath("//button[contains(text(), 'Заказать')]");

    // Попап
    private final By successPopup = By.xpath("//*[contains(text(), 'Заказ оформлен')]");

    public OrderFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ✅ Закрыть cookie banner
    public void acceptCookies() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement banner = shortWait.until(ExpectedConditions.presenceOfElementLocated(cookieBanner));
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='none';", banner);
        } catch (Exception e) {
            // Cookie banner может отсутствовать - это нормально
        }
    }

    // ✅ Шаг 1: заполняем форму стандартными методами
    public void fillStep1(String name, String surname, String address, String metro, String phone) {
        acceptCookies();

        // Ждём и заполняем поля по очереди
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput)).sendKeys(name);
        driver.findElement(surnameInput).sendKeys(surname);
        driver.findElement(addressInput).sendKeys(address);

        // Метро с автодополнением
        WebElement metroField = driver.findElement(metroInput);
        metroField.sendKeys(metro);

        // Пробуем выбрать из выпадающего списка метро
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement option = shortWait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".Select__option-text, .dropdown__option, [role='option']")
            ));
            option.click();
        } catch (Exception e) {
            // Если автодополнение не появилось - просто продолжаем
        }

        driver.findElement(phoneInput).sendKeys(phone);
    }

    // ✅ Переход к шагу 2
    public void clickNext() {
        acceptCookies();

        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
        btn.click();

        // Ждём появления полей шага 2
        wait.until(ExpectedConditions.presenceOfElementLocated(dateInput));
    }

    // ✅ Шаг 2: заполняем данные доставки
    public void fillStep2(String date, String period, String color, String comment) {
        // Дата
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateInput)).sendKeys(date);

        // Срок аренды
        try {
            WebElement dropdown = driver.findElement(periodDropdown);
            dropdown.click();
            driver.findElement(By.xpath("//div[text()='" + period + "']")).click();
        } catch (Exception e) {
            // Пробуем альтернативный способ выбора срока
            try {
                dropdownSelectByText(periodDropdown, period);
            } catch (Exception ex) {
                throw new RuntimeException("Не удалось выбрать срок аренды: " + period, ex);
            }
        }

        // Цвет самоката
        if ("black".equalsIgnoreCase(color)) {
            wait.until(ExpectedConditions.elementToBeClickable(colorBlack)).click();
        } else {
            wait.until(ExpectedConditions.elementToBeClickable(colorGrey)).click();
        }

        // Комментарий (необязательное поле)
        try {
            driver.findElement(commentInput).sendKeys(comment);
        } catch (Exception e) {
            // Комментарий может отсутствовать - это нормально
        }
    }

    // ✅ Отправка заказа
    public void submitOrder() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
        btn.click();
    }

    // ✅ Проверка успешного оформления
    public boolean isSuccessPopupDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successPopup)).isDisplayed();
    }

    // Вспомогательный метод для выбора из dropdown
    private void dropdownSelectByText(By dropdownLocator, String text) {
        WebElement dropdown = driver.findElement(dropdownLocator);
        dropdown.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(text(), '" + text + "')]")
        )).click();
    }
}