package pages;

import base.Attachments;
import io.qameta.allure.Allure;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchResultsPageVerifier {
    private final SearchResultsPageAction searchResultsPageAction;

    public SearchResultsPageVerifier(SearchResultsPageAction searchResultsPageAction) {
        this.searchResultsPageAction = searchResultsPageAction;
    }

    public void isPresentProduct() {
        Allure.step("Validate Product Title", () -> {
            Attachments.saveScreenshot("Before validation Product Title");

            String actualTitle = searchResultsPageAction.parseProductAndGetTitle();
            Allure.addAttachment("Actual Title", actualTitle);
            Allure.addAttachment("Expected to Contain", searchResultsPageAction.getQuery());

            assertTrue(searchResultsPageAction.getFound().isPresent(), "No product found with name containing '" + searchResultsPageAction.getQuery() + "'");
        });
    }
}
