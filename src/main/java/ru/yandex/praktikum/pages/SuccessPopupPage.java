package ru.yandex.praktikum.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class SuccessPopupPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ✅ Элементы попапа успеха:
    // Заголовок "Заказ оформлен"
    // Текст сообщения
    // Кнопка "Посмотреть статус"

    private By popupHeader = By.className("OrderModal__title");
    private By popupMessage = By.className("OrderModal__description");

    public SuccessPopupPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /** Проверить, что попап успеха отображается */
    public boolean isPopupDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(popupHeader)).isDisplayed();
    }

    /** Получить текст заголовка попапа */
    public String getPopupHeader() {
        return driver.findElement(popupHeader).getText();
    }

    /** Получить текст сообщения попапа */
    public String getPopupMessage() {
        return driver.findElement(popupMessage).getText();
    }
}
