package ru.praktikum.tests;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.praktikum.pageobjects.MainPage;

public class FaqTest extends BaseTest {

    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("faqData")
    void answerShouldBeDisplayedAfterClickOnQuestion(String question, String expectedAnswer) {
        MainPage mainPage = new MainPage(driver);

        mainPage.open();
        mainPage.openFaqItem(question);

        String actualAnswer = mainPage.getFaqAnswer(question);
        Assertions.assertTrue(
                actualAnswer.contains(expectedAnswer),
                "Ответ на вопрос '" + question + "' не совпал с ожидаемым"
        );
    }

    private static Stream<Arguments> faqData() {
        return Stream.of(
                Arguments.of("Сколько это стоит? И как оплатить?",
                        "Сутки — 400 рублей. Оплата курьеру — наличными или картой."),
                Arguments.of("Хочу сразу несколько самокатов! Так можно?",
                        "Пока что у нас так: один заказ — один самокат."),
                Arguments.of("Как рассчитывается время аренды?",
                        "Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру."),
                Arguments.of("Можно ли заказать самокат прямо на сегодня?",
                        "Только начиная с завтрашнего дня."),
                Arguments.of("Можно ли продлить заказ или вернуть самокат раньше?",
                        "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."),
                Arguments.of("Вы привозите зарядку вместе с самокатом?",
                        "Самокат приезжает к вам с полной зарядкой."),
                Arguments.of("Можно ли отменить заказ?",
                        "Да, пока самокат не привезли."),
                Arguments.of("Я жизу за МКАДом, привезёте?",
                        "Да, обязательно. Всем самокатов! И Москве, и Московской области.")
        );
    }
}
