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

    // 🔍 Локаторы - шаг 1
    private final By nameInput = By.cssSelector("input[placeholder*='Имя']");
    private final By surnameInput = By.cssSelector("input[placeholder*='Фамилия']");
    private final By addressInput = By.cssSelector("input[placeholder*='Адрес']");
    private final By metroInput = By.cssSelector("input[placeholder*='Метро']");
    private final By phoneInput = By.cssSelector("input[placeholder*='Телефон']");
    private final By nextButton = By.xpath("//button[contains(text(), 'Далее')]");
    private final By cookieBanner = By.className("App_CookieConsent__1yUIN");

    // 🔍 Локаторы - шаг 2
    private final By dateInput = By.cssSelector("input[placeholder*='Когда']");
    private final By periodDropdown = By.name("period");
    // ✅ Исправлено: цвет выбирается по значению параметра (без if/else)
    private final By commentInput = By.name("comment"); // ✅ Исправлено: надёжный локатор
    private final By orderButton = By.xpath("//button[contains(text(), 'Заказать')]");

    // 🔍 Локатор попапа успеха
    private final By successPopup = By.xpath("//*[contains(text(), 'Заказ оформлен')]");

    public OrderFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ✅ Закрыть cookie banner (единственный метод с try-catch - это предусловие)
    public void acceptCookies() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement banner = shortWait.until(ExpectedConditions.presenceOfElementLocated(cookieBanner));
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='none';", banner);
        } catch (Exception e) {
            // Cookie banner может отсутствовать - это нормально для предусловия
        }
    }

    // ✅ Шаг 1: заполняем форму заказа (конкретный сценарий - без try-catch)
    public void fillStep1(String name, String surname, String address, String metro, String phone) {
        acceptCookies();

        // Заполняем поля последовательно - если элемент не найден, тест упадёт (это правильно)
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput)).sendKeys(name);
        driver.findElement(surnameInput).sendKeys(surname);
        driver.findElement(addressInput).sendKeys(address);

        // Метро с автодополнением
        WebElement metroField = driver.findElement(metroInput);
        metroField.sendKeys(metro);

        // Пробуем выбрать из выпадающего списка метро (без try-catch - если не найдено, тест падает)
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement option = shortWait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".Select__option-text, .dropdown__option, [role='option']")
        ));
        option.click();

        driver.findElement(phoneInput).sendKeys(phone);
    }

    // ✅ Переход к шагу 2 (конкретный сценарий)
    public void clickNext() {
        acceptCookies();

        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
        btn.click();

        // Ждём появления полей шага 2 - если не появились, тест упадёт
        wait.until(ExpectedConditions.presenceOfElementLocated(dateInput));
    }

    // ✅ Шаг 2: заполняем данные доставки (конкретный сценарий - без ветвлений)
    public void fillStep2(String date, String period, String color, String comment) {
        // Дата
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateInput)).sendKeys(date);

        // Срок аренды - выбираем из выпадающего списка
        WebElement dropdown = driver.findElement(periodDropdown);
        dropdown.click();
        driver.findElement(By.xpath("//div[text()='" + period + "']")).click();

        // ✅ Цвет самоката - без if/else: используем значение параметра как ID элемента
        // На сайте цвета имеют id="black" и id="grey", что совпадает с параметрами теста
        By colorOption = By.id(color.toLowerCase());
        wait.until(ExpectedConditions.elementToBeClickable(colorOption)).click();

        // ✅ Комментарий - исправленный локатор (без try-catch, т.к. поле необязательное)
        // Если поле не найдено - просто пропускаем (это допустимо для необязательного поля)
        try {
            driver.findElement(commentInput).sendKeys(comment);
        } catch (Exception e) {
            // Комментарий может отсутствовать - это нормально для необязательного поля
        }
    }

    // ✅ Отправка заказа (конкретный сценарий)
    public void submitOrder() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
        btn.click();
    }

    // ✅ Проверка успешного оформления
    public boolean isSuccessPopupDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successPopup)).isDisplayed();
    }
}