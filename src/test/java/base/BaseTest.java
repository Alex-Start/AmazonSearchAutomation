package base;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.ProductData;

public class BaseTest {
    private Action action;

    @BeforeAll
    static void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-geolocation", "--lang=en_US");
        Configuration.browserCapabilities = options;

        Configuration.browser = "chrome";
        Configuration.timeout = 20000;
        Configuration.pageLoadStrategy = "normal";
        Configuration.screenshots = true;
        Configuration.savePageSource = false;

        AllureSelenideListener.setup();
    }

    protected Action newAction() {
        return action = new Action();
    }

    protected Action newAction(Action action) {
        return this.action = action;
    }

    protected Action getAction() {
        if (action == null) {
            return action = new Action();
        }
        return action;
    }

    protected Verifier getVerifier() {
        return new Verifier();
    }

    protected ProductData getProductData() {
        return getAction().searchResult().getProductData();
    }

    protected void logProductInfo() {
        ProductData product = getProductData();
        if(product == null) {
            Attachments.warn("Not found Product");
            return;
        }
        Allure.step("Product details:", () -> {
            Allure.addAttachment("Details",
                    String.format(
                            "Title: %s\nPrice: %s\nBrand: %s\nRating: %s",
                            product.title(), product.price(), product.brand(), product.rating())
            );

            Attachments.screenshotElement("Screenshot", product.product());
        });
    }

}
