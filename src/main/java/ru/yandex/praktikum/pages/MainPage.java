package ru.yandex.praktikum.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

public class MainPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ✅ ЛОКАТОРЫ

    // Кнопки "Заказать"
    private By orderButton = By.xpath("//button[contains(text(), 'Заказать')]");

    // Секция с вопросами - ищем по заголовку
    private By faqSection = By.xpath("//*[contains(text(), 'Вопросы о важном')]");

    // Элементы аккордеона - ищем по data-атрибуту или классу
    // Используем универсальный селектор
    private By accordionItem = By.cssSelector("[data-accordion-component='AccordionItem'], .accordion_item, [class*='accordion__item'], [class*='accordion-item']");

    // Логотипы
    private By samokatLogo = By.cssSelector("a[href='/'], img[alt*='Самокат'], [class*='logo']");
    private By yandexLogo = By.cssSelector("a[href*='yandex.ru'], img[alt*='Яндекс']");

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

        // 3. Ждём появления элементов аккордеона
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(accordionItem));

        // 4. Находим все элементы
        List<WebElement> items = driver.findElements(accordionItem);

        // 5. Проверяем, что элементов достаточно
        if (items.isEmpty()) {
            throw new RuntimeException("Элементы аккордеона не найдены на странице");
        }

        if (index >= items.size()) {
            throw new RuntimeException("Индекс " + index + " вне диапазона. Найдено: " + items.size());
        }

        // 6. Кликаем по элементу
        WebElement item = items.get(index);
        scrollIntoView(item);
        item.click();

        // 7. Небольшая пауза для анимации
        try { Thread.sleep(500); } catch (InterruptedException e) {}
    }

    // ✅ Получить текст
    public String getAccordionText(int index) {
        List<WebElement> items = driver.findElements(accordionItem);
        return items.get(index).getText();
    }

    // ✅ Проверить видимость
    public boolean isAccordionTextVisible(int index) {
        List<WebElement> items = driver.findElements(accordionItem);
        return !items.get(index).getText().isEmpty();
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
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", element);
    }
}