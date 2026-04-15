package ru.yandex.praktikum.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.pages.MainPage;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class DropdownTest extends BaseTest {

    private final int questionIndex;
    private final String expectedText;

    public DropdownTest(int questionIndex, String expectedText) {
        this.questionIndex = questionIndex;
        this.expectedText = expectedText;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                // ✅ ПРАВИЛЬНЫЕ тексты в правильном порядке (из реального сайта):
                {0, "400 рублей"},           // Вопрос 1: "Сколько это стоит?"
                {1, "один заказ — один самокат"},  // Вопрос 2: "Хочу сразу несколько самокатов?"
                {2, "8 мая"},                // Вопрос 3: "Как рассчитывается время аренды?"
                {3, "завтрашнего дня"},      // Вопрос 4: "Можно ли заказать самокат прямо на сегодня?"
                {4, "позвонить в поддержку"}, // Вопрос 5: "Можно ли продлить заказ?"
                {5, "восемь суток"},         // Вопрос 6: "Вы привозите зарядку?"
                {6, "пока самокат не привезли"}, // Вопрос 7: "Можно ли отменить заказ?"
                {7, "Московской области"}    // Вопрос 8: "Я живу за МКАДом?"
        });
    }

    @Test
    public void testAccordionItemOpens() {
        MainPage mainPage = new MainPage(driver);

        // Открываем вопрос
        mainPage.openAccordionItem(questionIndex);

        // Получаем текст (всё содержимое элемента: вопрос + ответ)
        String actualText = mainPage.getAccordionText(questionIndex);

        // Проверяем, что текст содержит ожидаемую фразу
        assertTrue("Ответ не содержит ожидаемый текст: '" + expectedText +
                        "'. Получено: '" + actualText + "'",
                actualText.contains(expectedText));
    }
}