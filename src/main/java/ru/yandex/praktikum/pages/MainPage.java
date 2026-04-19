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

    // 🔍 Локаторы
    private final By orderButton = By.xpath("//button[contains(text(), 'Заказать')]");
    private final By faqSection = By.xpath("//*[contains(text(), 'Вопросы о важном')]");
    private final By accordionItem = By.cssSelector("[data-accordion-component='AccordionItem'], .accordion_item, [class*='accordion__item'], [class*='accordion-item']");
    // ✅ Локатор для текста ответа (внутри аккордеона)
    private final By accordionContent = By.cssSelector(".AccordionItem__content, [class*='accordion__content'], [class*='accordion-content']");

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

        // 5. Валидация индекса
        if (index >= items.size()) {
            throw new IndexOutOfBoundsException("Индекс " + index + " вне диапазона. Найдено элементов: " + items.size());
        }

        // 6. Кликаем по элементу
        WebElement item = items.get(index);
        scrollIntoView(item);
        wait.until(ExpectedConditions.elementToBeClickable(item)).click();
    }

    // ✅ Получить текст ТОЛЬКО ответа (без вопроса)
    public String getAccordionText(int index) {
        List<WebElement> items = driver.findElements(accordionItem);
        WebElement item = items.get(index);

        // Ищем контент ответа внутри элемента аккордеона
        try {
            WebElement content = item.findElement(accordionContent);
            return content.getText();
        } catch (Exception e) {
            // Если не нашли по классу контента, возвращаем весь текст (fallback)
            return item.getText();
        }
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

    // ✅ Переход на страницу заказа
    public OrderFormPage goToOrderForm() {
        clickHeaderOrderButton();
        return new OrderFormPage(driver);
    }

    // ✅ Вспомогательный метод
    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}