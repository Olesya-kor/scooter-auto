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

    // Кнопка "Заказать"
    private final By orderButton = By.xpath("//button[contains(text(), 'Заказать')]");

    // Заголовок блока FAQ
    private final By faqSection = By.xpath("//*[contains(text(), 'Вопросы о важном')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void openAccordionItem(int index) {
        WebElement item = getAccordionItem(index);
        wait.until(ExpectedConditions.elementToBeClickable(item)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocator(index)));
    }

    public String getAccordionText(int index) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocator(index))).getText();
    }

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

    private WebElement getAccordionItem(int index) {
        WebElement section = wait.until(ExpectedConditions.visibilityOfElementLocated(faqSection));
        scrollIntoView(section);

        WebElement item = wait.until(ExpectedConditions.elementToBeClickable(questionLocator(index)));
        scrollIntoView(item);
        return item;
    }

    private By questionLocator(int index) {
        return By.id("accordion__heading-" + index);
    }

    private By answerLocator(int index) {
        return By.id("accordion__panel-" + index);
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
