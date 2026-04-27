package ru.praktikum.pageobjects;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class OrderPage extends BasePage {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    // Заголовок первого шага формы
    private static final By CONTACTS_HEADER = By.xpath("//div[normalize-space()='Для кого самокат']");

    // Заголовок второго шага формы
    private static final By RENT_HEADER = By.xpath("//div[normalize-space()='Про аренду']");

    // Поле "Имя"
    private static final By FIRST_NAME_INPUT = By.xpath("//input[@placeholder='* Имя']");

    // Поле "Фамилия"
    private static final By LAST_NAME_INPUT = By.xpath("//input[@placeholder='* Фамилия']");

    // Поле "Адрес: куда привезти заказ"
    private static final By ADDRESS_INPUT = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");

    // Поле выбора станции метро
    private static final By METRO_INPUT = By.cssSelector("input.select-search__input");

    // Поле "Телефон: на него позвонит курьер"
    private static final By PHONE_INPUT = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");

    // Кнопка "Далее"
    private static final By NEXT_BUTTON = By.xpath("//button[normalize-space()='Далее']");

    // Поле "Когда привезти самокат"
    private static final By DELIVERY_DATE_INPUT = By.xpath("//input[@placeholder='* Когда привезти самокат']");

    // Выпадающий список "Срок аренды"
    private static final By RENT_TIME_DROPDOWN = By.cssSelector(".Dropdown-control");

    // Чекбокс цвета "чёрный жемчуг"
    private static final By BLACK_COLOR_CHECKBOX = By.xpath("//label[@for='black']");

    // Чекбокс цвета "серая безысходность"
    private static final By GREY_COLOR_CHECKBOX = By.xpath("//label[@for='grey']");

    // Поле "Комментарий для курьера"
    private static final By COMMENT_INPUT = By.xpath("//input[@placeholder='Комментарий для курьера']");

    // Кнопка "Заказать"
    private static final By CREATE_ORDER_BUTTON = By.xpath("//button[normalize-space()='Заказать']");

    // Модальное окно подтверждения заказа
    private static final By CONFIRM_MODAL = By.xpath("//*[contains(.,'Хотите оформить заказ?')]");

    // Кнопка "Да"
    private static final By CONFIRM_YES_BUTTON = By.xpath("//button[normalize-space()='Да']");

    // Модальное окно успешного оформления заказа
    private static final By SUCCESS_MODAL = By.xpath("//*[contains(.,'Заказ оформлен')]");

    // Текст с номером заказа
    private static final By SUCCESS_MODAL_TEXT = By.xpath("//*[contains(.,'Заказ оформлен')]/following::div[contains(.,'Номер заказа')][1]");

    public OrderPage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilOpened() {
        waitForVisible(CONTACTS_HEADER);
    }

    public void waitForRentStep() {
        waitForVisible(RENT_HEADER);
    }

    public void fillContacts(String firstName, String lastName, String address, String metroStation, String phone) {
        type(FIRST_NAME_INPUT, firstName);
        type(LAST_NAME_INPUT, lastName);
        type(ADDRESS_INPUT, address);
        type(METRO_INPUT, metroStation);
        click(metroOption(metroStation));
        type(PHONE_INPUT, phone);
    }

    public void clickNextButton() {
        clickWithScroll(NEXT_BUTTON);
    }

    public void fillRentInfo(LocalDate deliveryDate,
                             String rentTime,
                             boolean blackColor,
                             boolean greyColor,
                             String comment) {
        WebElement dateInput = waitForVisible(DELIVERY_DATE_INPUT);
        dateInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        dateInput.sendKeys(deliveryDate.format(DATE_FORMATTER));
        dateInput.sendKeys(Keys.ENTER);

        click(RENT_TIME_DROPDOWN);
        click(rentTimeOption(rentTime));

        if (blackColor) {
            click(BLACK_COLOR_CHECKBOX);
        }

        if (greyColor) {
            click(GREY_COLOR_CHECKBOX);
        }

        if (comment != null && !comment.isBlank()) {
            type(COMMENT_INPUT, comment);
        }
    }

    public void clickCreateOrderButton() {
        clickWithScroll(CREATE_ORDER_BUTTON);
        waitForVisible(CONFIRM_MODAL);
    }

    public void confirmOrder() {
        click(CONFIRM_YES_BUTTON);
    }

    public void waitForSuccessModal() {
        waitForVisible(SUCCESS_MODAL);
    }

    public String getSuccessModalText() {
        return getText(SUCCESS_MODAL_TEXT);
    }

    private By metroOption(String stationName) {
        return By.xpath("//button[contains(@class,'select-search__option')][.//*[normalize-space()="
                + escapeXPathText(stationName) + "]]");
    }

    private By rentTimeOption(String rentTime) {
        return By.xpath("//*[contains(@class,'Dropdown-option') and normalize-space()="
                + escapeXPathText(rentTime) + "]");
    }
}
