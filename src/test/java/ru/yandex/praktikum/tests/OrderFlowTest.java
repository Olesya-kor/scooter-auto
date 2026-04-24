package ru.yandex.praktikum.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.pages.MainPage;
import ru.yandex.praktikum.pages.OrderFormPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderFlowTest extends BaseTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final String name;
    private final String surname;
    private final String address;
    private final String metro;
    private final String phone;
    private final int daysToDelivery;
    private final String period;
    private final String color;
    private final String comment;
    private final Consumer<MainPage> orderButtonAction;
    private final String entryPointName;

    public OrderFlowTest(
            String name,
            String surname,
            String address,
            String metro,
            String phone,
            int daysToDelivery,
            String period,
            String color,
            String comment,
            Consumer<MainPage> orderButtonAction,
            String entryPointName
    ) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.daysToDelivery = daysToDelivery;
        this.period = period;
        this.color = color;
        this.comment = comment;
        this.orderButtonAction = orderButtonAction;
        this.entryPointName = entryPointName;
    }

    @Parameterized.Parameters(name = "{index}: заказ через {10}")
    public static Collection<Object[]> orderData() {
        return Arrays.asList(new Object[][]{
                {
                        "Иван", "Иванов", "Москва, Тверская 1", "Комсомольская",
                        "+79991234567", 1, "сутки", "black",
                        "Без комментариев", (Consumer<MainPage>) MainPage::clickHeaderOrderButton, "верхнюю кнопку"
                },
                {
                        "Мария", "Петрова", "Москва, Арбат 10", "Черкизовская",
                        "+79997654321", 2, "двое суток", "grey",
                        "Позвоните за час", (Consumer<MainPage>) MainPage::clickFooterOrderButton, "нижнюю кнопку"
                }
        });
    }

    @Test
    public void testPositiveOrderFlow_userCanCompleteOrder() {
        MainPage mainPage = new MainPage(driver);
        orderButtonAction.accept(mainPage);

        OrderFormPage orderFormPage = new OrderFormPage(driver);
        orderFormPage.fillStep1(name, surname, address, metro, phone);
        orderFormPage.clickNext();
        orderFormPage.fillStep2(deliveryDate(), period, color, comment);
        orderFormPage.submitOrder();

        assertTrue(
                "Заказ не оформлен: не появился попап 'Заказ оформлен' для точки входа " + entryPointName,
                orderFormPage.isSuccessPopupDisplayed()
        );
    }

    private String deliveryDate() {
        return LocalDate.now().plusDays(daysToDelivery).format(DATE_FORMATTER);
    }
}
