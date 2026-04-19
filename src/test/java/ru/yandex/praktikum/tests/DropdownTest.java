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
                // ✅ Вопрос + Ответ (полный текст)
                {0, "Сколько это стоит? И как оплатить?\nСутки — 400 рублей. Оплата курьеру — наличными или картой."},
                {1, "Хочу сразу несколько самокатов! Так можно?\nПока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."},
                {2, "Как рассчитывается время аренды?\nДопустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."},
                {3, "Можно ли заказать самокат прямо на сегодня?\nТолько начиная с завтрашнего дня. Но скоро станем расторопнее."},
                {4, "Можно ли продлить заказ или вернуть самокат раньше?\nПока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."},
                {5, "Вы привозите зарядку вместе с самокатом?\nСамокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."},
                {6, "Можно ли отменить заказ?\nДа, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."},
                {7, "Я живу за МКАДом, привезёте?\nДа, обязательно. Всем самокатов! И Москве, и Московской области."}
        });
    }

    @Test
    public void testAccordionItemOpens() {
        MainPage mainPage = new MainPage(driver);

        // Открываем вопрос
        mainPage.openAccordionItem(questionIndex);

        // Получаем полный текст (вопрос + ответ)
        String actualText = mainPage.getAccordionText(questionIndex);

        // ✅ Проверяем, что текст содержит ожидаемый вопрос и ответ
        assertTrue("Текст не содержит ожидаемый: '" + expectedText +
                        "'. Получено: '" + actualText + "'",
                actualText.contains(expectedText));
    }
}