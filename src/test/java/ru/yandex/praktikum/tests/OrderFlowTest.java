package ru.yandex.praktikum.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.pages.MainPage;
import ru.yandex.praktikum.pages.OrderFormPage;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;


@RunWith(Parameterized.class)
public class OrderFlowTest extends BaseTest {

    private final String name;
    private final String surname;
    private final String address;
    private final String metro;
    private final String phone;
    private final String date;
    private final String period;
    private final String color;
    private final String comment;
    private final String entryPoint;

    public OrderFlowTest(String name, String surname, String address, String metro,
                         String phone, String date, String period, String color,
                         String comment, String entryPoint) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.date = date;
        this.period = period;
        this.color = color;
        this.comment = comment;
        this.entryPoint = entryPoint;
    }

    @Parameterized.Parameters(name = "{index}: Заказ через {9} - {0} {1}, метро {3}, цвет {7}")
    public static Collection<Object[]> orderData() {
        return Arrays.asList(new Object[][] {
                {
                        "Иван", "Иванов", "Москва, Тверская 1", "Комсомольская",
                        "+79991234567", "20.12.2024", "Трое суток", "black",
                        "Без комментариев", "шапку"
                },
                {
                        "Мария", "Петрова", "СПб, Невский 10", "Невский проспект",
                        "+79997654321", "25.12.2024", "Пять суток", "grey",
                        "Позвоните за час", "подвал"
                }
        });
    }

    @Test
    public void testPositiveOrderFlow_userCanCompleteOrder() {
        // 🔹 Дано: пользователь на главной странице
        MainPage mainPage = new MainPage(driver);

        // 🔹 Когда: нажимает кнопку "Заказать" (точка входа)
        if ("шапку".equals(entryPoint)) {
            mainPage.clickHeaderOrderButton();
        } else {
            mainPage.clickFooterOrderButton();
        }

        // 🔹 И: заполняет форму заказа (шаг 1)
        OrderFormPage form = new OrderFormPage(driver);
        form.fillStep1(name, surname, address, metro, phone);

        // 🔹 И: переходит к шагу 2
        form.clickNext();

        // 🔹 И: заполняет данные доставки (шаг 2)
        form.fillStep2(date, period, color, comment);

        // 🔹 И: отправляет заказ
        form.submitOrder();

        // 🔹 Тогда: появляется попап об успешном оформлении
        assertTrue("Заказ не оформлен: не появился попап 'Заказ оформлен'",
                form.isSuccessPopupDisplayed());
    }
}