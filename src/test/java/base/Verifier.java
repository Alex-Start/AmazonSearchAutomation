package base;

import io.qameta.allure.Allure;
import pages.HomePageVerifier;
import pages.ProductData;
import pages.SearchResultsPageAction;
import pages.SearchResultsPageVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Verifier {

    public HomePageVerifier homePage() {
        return new HomePageVerifier();
    }

    public SearchResultsPageVerifier searchPage(SearchResultsPageAction searchResultsPageAction) {
        return new SearchResultsPageVerifier(searchResultsPageAction);
    }

    public void validateProduct(ProductData actual, ProductData expected) {

        Allure.step("Validate Product Title", () -> {
            if (expected.title() == null) {
                Allure.addAttachment("Info", "Skip Title verification");
            } else {
                Attachments.attachComparison(actual.title(), expected.title());

                assertTrue(
                        actual.title().toLowerCase().contains(expected.title().toLowerCase()),
                        "Product title does not match expected value"
                );
            }
        });

        Allure.step("Validate Product Brand", () -> {
            if (expected.brand() == null) {
                Allure.addAttachment("Info", "Skip Brand verification");
            } else {
                Attachments.attachComparison(actual.brand(), expected.brand());

                assertEquals(
                        expected.brand().toLowerCase(),
                        actual.brand().toLowerCase(),
                        "Brand mismatch"
                );
            }
        });

        Allure.step("Validate Product Price", () -> {
            if (expected.price() == null) {
                Allure.addAttachment("Info", "Skip Price verification");
            } else {
                Attachments.attachComparison(actual.price(), expected.price());

                double actualPrice = parsePrice(actual.price());
                double expectedPrice = parsePrice(expected.price());
                String actualCurrency = parseCurrency(actual.price());
                String expectedCurrency = parseCurrency(expected.price());

                assertEquals(expectedPrice, actualPrice,
                        "Price mismatch");
                assertEquals(expectedCurrency, actualCurrency,
                        "Currency mismatch");
            }
        });

        Allure.step("Validate Product Rating", () -> {
            if (expected.rating() == null) {
                Allure.addAttachment("Info", "Skip Rating verification");
            } else {
                Attachments.attachComparison(actual.rating(), expected.rating());

                assertEquals(
                        Double.parseDouble(expected.rating().toLowerCase()),
                        Double.parseDouble(actual.rating().toLowerCase()),
                        "Rating mismatch"
                );
            }
        });
    }

    private double parsePrice(String price) {
        if (price == null || price.isEmpty()) return 0;
        String currency = parseCurrency(price);

        String cleaned = price.replaceAll("[^0-9.,’']", "");
        switch (currency) {

            case "$":     // USD, CAD, AUD
            case "£":     // GBP
            case "₹":     // India
            case "¥":     // Japan  // No decimals
                // "1,299.99" → remove comma, keep dot
                cleaned = cleaned.replace(",", "");
                return Double.parseDouble(cleaned);

            case "€":     // EUR
            case "R$":    // BRL
                // "1.299,99" → remove thousand "." → replace "," with "."
                cleaned = cleaned.replace(".", "").replace(",", ".");
                return Double.parseDouble(cleaned);

            case "CHF":   // Switzerland
                // "1’299.00" → remove apostrophe thousand separator
                cleaned = cleaned.replace("’", "");
                return Double.parseDouble(cleaned);

            default:
                // Fallback universal logic
                return parseUniversal(price);
        }
    }

    private double parseUniversal(String price) {
        String cleaned = price.replaceAll("[^0-9.,]", "");

        if (cleaned.contains(",") && cleaned.contains(".")) {
            // decide based on last separator
            if (cleaned.lastIndexOf(',') > cleaned.lastIndexOf('.')) {
                cleaned = cleaned.replace(".", "").replace(",", ".");
            } else {
                cleaned = cleaned.replace(",", "");
            }
        } else if (cleaned.contains(",")) {
            cleaned = cleaned.replace(",", ".");
        }

        return Double.parseDouble(cleaned);
    }

    private String parseCurrency(String price) {
        if (price == null || price.isEmpty()) {
            return "";
        }
        // Keep only currency symbol or letters
        String currency = price.replaceAll("[0-9.,’' ]", "").trim();

        // If something like "US$" or "CDN$"
        if (currency.length() > 1 && currency.contains("$")) {
            currency  = "$";
        }

        return currency;
    }
}
