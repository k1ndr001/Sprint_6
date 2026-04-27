package ru.praktikum.tests;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.praktikum.pageobjects.MainPage;
import ru.praktikum.pageobjects.OrderPage;

public class OrderFlowTest extends BaseTest {

    @ParameterizedTest(name = "{index}. {0}: {1} {2}")
    @MethodSource("orderData")
    void scooterCanBeOrderedFromBothEntryPoints(String entryPointName,
                                                boolean useTopButton,
                                                String firstName,
                                                String lastName,
                                                String address,
                                                String metroStation,
                                                String phone,
                                                LocalDate deliveryDate,
                                                String rentTime,
                                                boolean blackColor,
                                                boolean greyColor,
                                                String comment) {
        Assumptions.assumeFalse(
                isChrome(),
                "Known application bug: order confirmation is broken in Chrome. Run OrderFlowTest with -Dbrowser=firefox."
        );

        MainPage mainPage = new MainPage(driver);
        OrderPage orderPage = new OrderPage(driver);

        mainPage.open();
        if (useTopButton) {
            mainPage.clickTopOrderButton();
        } else {
            mainPage.clickBottomOrderButton();
        }

        orderPage.waitUntilOpened();
        orderPage.fillContacts(firstName, lastName, address, metroStation, phone);
        orderPage.clickNextButton();
        orderPage.waitForRentStep();
        orderPage.fillRentInfo(deliveryDate, rentTime, blackColor, greyColor, comment);
        orderPage.clickCreateOrderButton();
        orderPage.confirmOrder();
        orderPage.waitForSuccessModal();

        Assertions.assertTrue(
                orderPage.getSuccessModalText().contains("Номер заказа"),
                "После оформления заказа не появилось окно с номером заказа"
        );
    }

    private static Stream<Arguments> orderData() {
        return Stream.of(
                Arguments.of(
                        "Верхняя кнопка",
                        true,
                        "Иван",
                        "Петров",
                        "Москва, ул. Ленина, 15",
                        "Черкизовская",
                        "+79991234567",
                        LocalDate.now().plusDays(3),
                        "сутки",
                        true,
                        false,
                        "Позвоните за 10 минут"
                ),
                Arguments.of(
                        "Нижняя кнопка",
                        false,
                        "Иван",
                        "Петров",
                        "Москва, ул. Ленина, 15",
                        "Черкизовская",
                        "+79991234567",
                        LocalDate.now().plusDays(3),
                        "сутки",
                        true,
                        false,
                        "Позвоните за 10 минут"
                ),
                Arguments.of(
                        "Верхняя кнопка",
                        true,
                        "Мария",
                        "Соколова",
                        "Москва, пр-т Мира, 42",
                        "Сокольники",
                        "+79997654321",
                        LocalDate.now().plusDays(5),
                        "двое суток",
                        false,
                        true,
                        "Домофон 42"
                ),
                Arguments.of(
                        "Нижняя кнопка",
                        false,
                        "Мария",
                        "Соколова",
                        "Москва, пр-т Мира, 42",
                        "Сокольники",
                        "+79997654321",
                        LocalDate.now().plusDays(5),
                        "двое суток",
                        false,
                        true,
                        "Домофон 42"
                )
        );
    }
}
