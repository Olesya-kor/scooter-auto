package ru.yandex.praktikum.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class MainPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ✅ ЛОКАТОРЫ
    private final By orderButton = By.xpath("//button[contains(text(), 'Заказать')]");
    private final By faqSection = By.xpath("//*[contains(text(), 'Вопросы о важном')]");
    private final By accordionItem = By.cssSelector("[data-accordion-component='AccordionItem'], .accordion_item, [class*='accordion__item'], [class*='accordion-item']");
    private final By samokatLogo = By.cssSelector("a[href='/'], img[alt*='Самокат'], [class*='logo']");
    private final By yandexLogo = By.cssSelector("a[href*='yandex.ru'], img[alt*='Яндекс']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ✅ Открыть вопрос
    public void openAccordionItem(int index) {
        // 1. Ждём появления секции с вопросами
        wait.until(ExpectedConditions.presenceOfElementLocated(faqSection));

        // 2. Прокручиваем к секции
        WebElement section = driver.findElement(faqSection);
        scrollIntoView(section);

        // 3. Ждём появления элементов аккордеона (гарантирует, что список не пуст)
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(accordionItem));

        // 4. Находим все элементы
        List<WebElement> items = driver.findElements(accordionItem);

        // 5. Валидация индекса (оставил как необходимую проверку границ, а не логику теста)
        if (index >= items.size()) {
            throw new IndexOutOfBoundsException("Индекс " + index + " вне диапазона. Найдено элементов: " + items.size());
        }

        // 6. Кликаем по элементу с явным ожиданием кликабельности (вместо Thread.sleep)
        WebElement item = items.get(index);
        scrollIntoView(item);
        wait.until(ExpectedConditions.elementToBeClickable(item)).click();
    }

    // ✅ Получить текст
    public String getAccordionText(int index) {
        List<WebElement> items = driver.findElements(accordionItem);
        return items.get(index).getText();
    }

    // ✅ Кнопки заказа
    public void clickHeaderOrderButton() {
        List<WebElement> buttons = driver.findElements(orderButton);
        WebElement button = buttons.get(0);
        scrollIntoView(button);
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    public void clickFooterOrderButton() {
        List<WebElement> buttons = driver.findElements(orderButton);
        WebElement button = buttons.get(buttons.size() - 1);
        scrollIntoView(button);
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    // ✅ Логотипы
    public void clickSamokatLogo() {
        driver.findElement(samokatLogo).click();
    }

    public void clickYandexLogo() {
        driver.findElement(yandexLogo).click();
    }

    public OrderFormPage goToOrderForm() {
        clickHeaderOrderButton();
        return new OrderFormPage(driver);
    }

    // ✅ Вспомогательный метод
    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}