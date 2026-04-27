package ru.praktikum.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainPage extends BasePage {

    private static final String PAGE_URL = "https://qa-scooter.education-services.ru/";

    // Заголовок главной страницы
    private static final By PAGE_TITLE = By.xpath("//*[contains(.,'Самокат') and contains(.,'пару дней')]");

    // Верхняя кнопка "Заказать"
    private static final By TOP_ORDER_BUTTON = By.xpath("(//button[normalize-space()='Заказать'])[1]");

    // Нижняя кнопка "Заказать"
    private static final By BOTTOM_ORDER_BUTTON = By.xpath("(//button[normalize-space()='Заказать'])[2]");

    // Заголовок блока "Вопросы о важном"
    private static final By FAQ_TITLE = By.xpath("//*[normalize-space()='Вопросы о важном']");

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(PAGE_URL);
        waitUntilOpened();
        acceptCookiesIfPresent();
    }

    public void waitUntilOpened() {
        waitForVisible(PAGE_TITLE);
    }

    public void clickTopOrderButton() {
        clickWithScroll(TOP_ORDER_BUTTON);
    }

    public void clickBottomOrderButton() {
        scrollIntoView(FAQ_TITLE);
        clickWithScroll(BOTTOM_ORDER_BUTTON);
    }

    public void openFaqItem(String question) {
        scrollIntoView(FAQ_TITLE);
        clickWithScroll(faqQuestionButton(question));
    }

    public String getFaqAnswer(String question) {
        return getText(faqAnswerPanel(question));
    }

    private By faqQuestionButton(String question) {
        return By.xpath("//*[contains(@class,'accordion__button') and normalize-space()="
                + escapeXPathText(question) + "]");
    }

    private By faqAnswerPanel(String question) {
        return By.xpath("//*[contains(@class,'accordion__button') and normalize-space()="
                + escapeXPathText(question)
                + "]/ancestor::*[contains(@class,'accordion__item')][1]//*[contains(@class,'accordion__panel')]");
    }
}
