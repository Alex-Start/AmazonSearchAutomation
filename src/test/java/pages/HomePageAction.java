package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class HomePageAction {
    private final String URLPAGE = "https://www.amazon.com/?language=en_US&currency=USD";
    private final SelenideElement hamburgerMenu = $("#nav-hamburger-menu");

    public HomePageAction openHome() {
        open(URLPAGE);
        return this;
    }

    public HomePageAction clickContinueShopping() {
        SelenideElement button = $$("button.a-button-text")
                .findBy(text("Continue shopping"));

        if (button.exists()) {
            button.click();
            waitForHamburgerMenu();
        }
        return this;
    }

    @Step("Search for product: {query}")
    public HomePageAction search(String query) {
        $("#twotabsearchtextbox").setValue(query).pressEnter();
        return this;
    }

    public HomePageAction setUSDLocationAndCurrency() {
        // Open the change region menu
        $("[id='icp-nav-flyout']").click();

        // Click “Change country/region”
        SelenideElement radio = $x("//input[@name='lop' and @value='en_US']");
        if (!radio.isSelected()) {
            radio.parent().click();
        }

        SelenideElement currencyPrompt =
                $("#icp-currency-dropdown-selected-item-prompt .a-dropdown-prompt");

        if (!currencyPrompt.getText().contains("USD")) {
            // show the list
            $("#icp-currency-dropdown-selected-item-prompt").click();
            // then choose USD from the dropdown
            $("#USD .a-dropdown-link").click();
        }

        // Save
        $("input[type='submit'][aria-labelledby='icp-save-button-announce']").click();
        waitForHamburgerMenu();
        return this;
    }

    public void waitForHamburgerMenu() {
        hamburgerMenu.shouldBe(visible, enabled);
    }

    public HomePageVerifier verifier() {
        return new HomePageVerifier();
    }
}