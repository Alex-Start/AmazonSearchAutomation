package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.ProductData;

import java.util.stream.Stream;

@Epic("Amazon")
@Feature("Product Search")
public class AmazonSearchTest extends BaseTest {

    static Stream<Arguments> productData() {
        return Stream.of(
                Arguments.of("asus rog xg mobile", "rog xg", new ProductData(
                        "ROG XG Mobile (2025) External Graphics Card, NVIDIA",
                        "$1299.99",
                        "Asus",
                        null,
                        null))
                //,Arguments.of("...", "...", new ProductData(...))
        );
    }

    @ParameterizedTest
    @MethodSource("productData")
    @Description("Search for product on Amazon")
    void searchTest(String searchField, String matchQuery, ProductData expectedProduct) {

        newAction().homePage()
                .openHome() // faced with 2 different page cases //TODO covered only 1
                .clickContinueShopping() // sometimes is absent
                .setUSDLocationAndCurrency()
                .search(searchField);

        newAction().searchResult()
                .findFirstProductByName(matchQuery)
                .verifier()
                .isPresentProduct();

        logProductInfo();

        // Validate expected vs actual
        getVerifier().validateProduct(getProductData(), expectedProduct);
    }
}
