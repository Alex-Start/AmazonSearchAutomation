package pages;

public record ProductData(String title, String price, String brand, String rating,
                          com.codeborne.selenide.SelenideElement product) {

}
