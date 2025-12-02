package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.Optional;

import static com.codeborne.selenide.Selenide.*;

public class SearchResultsPageAction {

    private final ElementsCollection PRODUCT_ELEMENTS = $$("div[data-component-type='s-search-result']");
    private String query; // store the last query
    private Optional<SelenideElement> found; // store the last search
    private ProductData productData; // store the last product

    public ElementsCollection getAllProducts() {
        return PRODUCT_ELEMENTS;
    }

    public ProductData getProductData() {
        if (found.isEmpty()) {
            return null;
        }
        found.ifPresent(selenideElement -> productData = parseProduct(selenideElement));
        return productData;
    }

    private void setProductData(ProductData productData) {
        this.productData = productData;
    }

    public Optional<SelenideElement> getFound() {
        return found;
    }

    public String getQuery() {
        return query;
    }

    /**
     * Find the first product that contains query in title.
     */
    public SearchResultsPageAction findFirstProductByName(String query) {
        this.query = query;
        found = PRODUCT_ELEMENTS
                .stream()
                .filter(p -> p.$("a h2 span").exists())
                .filter(p -> p.$("a h2 span").getText().toLowerCase().contains(query.toLowerCase()))
                .findFirst();
        return this;
    }

    /**
     * Extract product data (title, price, brand, rating, etc.)
     */
    public ProductData parseProduct(SelenideElement product) {
        String title = product.$("a h2 span").text();

        String price = "";
        if (product.$(".a-price .a-offscreen").exists()) {
            price = product.$(".a-price .a-offscreen").innerText();
        }

        String brand = "";
        if (product.$(".a-row h2 span").exists()) {
            brand = product.$(".a-row h2 span").innerText();
        }

        String rating = "";
        if (product.$("div[data-cy='reviews-block']").exists()) {
            rating = product.$("div[data-cy='reviews-block']").text().split("\n")[0]; // Example: "4.0"
        }

        return new ProductData(title, price, brand, rating, product);
    }

    public String parseProductAndGetTitle() {
        String actualTitle = "";
        if (getFound().isPresent()) {
            setProductData(parseProduct(getFound().get()));
            actualTitle = getProductData().title();
        }
        return actualTitle;
    }

    public SearchResultsPageVerifier verifier() {
        return new SearchResultsPageVerifier(this);
    }
}
