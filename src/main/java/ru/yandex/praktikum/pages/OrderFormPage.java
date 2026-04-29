package ru.yandex.praktikum.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderFormPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Поле "Имя"
    private final By nameInput = By.cssSelector("input[placeholder='* Имя']");

    // Поле "Фамилия"
    private final By surnameInput = By.cssSelector("input[placeholder='* Фамилия']");

    // Поле "Адрес"
    private final By addressInput = By.cssSelector("input[placeholder='* Адрес: куда привезти заказ']");

    // Поле "Станция метро"
    private final By metroInput = By.cssSelector("input.select-search__input[placeholder='* Станция метро']");

    // Поле "Телефон"
    private final By phoneInput = By.cssSelector("input[placeholder='* Телефон: на него позвонит курьер']");

    // Кнопка "Далее"
    private final By nextButton = By.xpath("//button[contains(text(), 'Далее')]");

    // Кнопка принятия cookies
    private final By cookieButton = By.id("rcc-confirm-button");

    // Поле "Когда привезти самокат"
    private final By dateInput = By.cssSelector("input[placeholder='* Когда привезти самокат']");

    // Попап календаря
    private final By datePickerPopup = By.cssSelector(".react-datepicker");

    // Выпадающий список "Срок аренды"
    private final By periodDropdown = By.cssSelector(".Dropdown-placeholder");

    // Поле "Комментарий для курьера"
    private final By commentInput = By.cssSelector("input[placeholder='Комментарий для курьера']");

    // Кнопка "Заказать" на втором шаге
    private final By orderButton = By.xpath("//div[contains(@class, 'Order_Buttons')]//button[contains(text(), 'Заказать')]");

    // Кнопка подтверждения "Да"
    private final By confirmOrderButton = By.xpath("//button[text()='Да']");

    // Попап успешного оформления
    private final By successPopup = By.xpath("//*[contains(text(), 'Заказ оформлен')]");

    // Первый вариант станции метро
    private final By firstMetroOption = By.cssSelector(".select-search__select .select-search__option");

    public OrderFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void acceptCookies() {
        if (!driver.findElements(cookieButton).isEmpty()) {
            click(cookieButton);
        }
    }

    public void fillStep1(String name, String surname, String address, String metro, String phone) {
        acceptCookies();

        type(nameInput, name);
        type(surnameInput, surname);
        type(addressInput, address);
        selectMetroStation(metro);
        type(phoneInput, phone);
    }

    public void clickNext() {
        click(nextButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateInput));
    }

    public void fillStep2(String date, String period, String color, String comment) {
        WebElement deliveryDate = wait.until(ExpectedConditions.elementToBeClickable(dateInput));
        deliveryDate.click();
        deliveryDate.clear();
        deliveryDate.sendKeys(date);
        closeDatePicker(deliveryDate);

        click(periodDropdown);
        click(By.xpath("//div[contains(@class, 'Dropdown-option') and text()='" + period + "']"));
        click(By.id(color.toLowerCase()));
        type(commentInput, comment);
    }

    public void submitOrder() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        scrollIntoView(button);
        button.click();
        click(confirmOrderButton);
    }

    public boolean isSuccessPopupDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successPopup)).isDisplayed();
    }

    private void selectMetroStation(String metro) {
        WebElement metroField = wait.until(ExpectedConditions.elementToBeClickable(metroInput));
        metroField.click();
        metroField.sendKeys(metro);

        if (!driver.findElements(firstMetroOption).isEmpty()) {
            click(firstMetroOption);
        } else {
            metroField.sendKeys(Keys.ARROW_DOWN);
            metroField.sendKeys(Keys.ENTER);
        }
    }

    private void closeDatePicker(WebElement deliveryDate) {
        deliveryDate.sendKeys(Keys.ENTER);
        deliveryDate.sendKeys(Keys.TAB);

        new Actions(driver).moveByOffset(10, 10).click().perform();

        if (!driver.findElements(datePickerPopup).isEmpty()) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(datePickerPopup));
        }
    }

    private void type(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }

    private void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollIntoView(element);
        element.click();
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}

