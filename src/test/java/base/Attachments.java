package base;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import com.codeborne.selenide.Selenide;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Attachments {

    public static void saveScreenshot(String name) {
        byte[] screenshot = Selenide.screenshot(OutputType.BYTES);
        try {
            assert screenshot != null;
            try (InputStream is = new ByteArrayInputStream(screenshot)) {
                Allure.addAttachment(name, "image/png", is, ".png");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Screenshot fails", e);
        }
    }

    @Attachment(value = "{name}", type = "image/png")
    public static byte[] screenshotElement(String name, SelenideElement element) {
        if (element == null) {
            warn("Element is null to screenshot");
            return new byte[0];
        }
        File file = element.screenshot();
        try {
            assert file != null;
            return Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            return new byte[0];
        }
    }

    @Attachment(value = "Comparison", type = "text/plain")
    public static String attachComparison(String actual, String expected) {
        return "Actual: " + actual + "\nExpected: " + expected;
    }

    @Step("⚠\uFE0F WARN: {message}")
    public static void warn(String message) {
        // logging to console
        System.out.println("⚠\uFE0F WARN: " + message);
    }
}
