package org.example.utils;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class POGenerator {

    public static JSONObject scanPage(WebDriver driver) {
        Map<String, String> elementMap = new LinkedHashMap<>();

        List<WebElement> elements = driver.findElements(By.xpath("//*[@type or @data_element_type or @class]"));

        for (WebElement element : elements) {
            try {
                System.out.println("Element: " + element.getTagName());
                String key = getReadableKey(element);
                String selector = getAccessibleCssSelector(element);

                if (!elementMap.containsKey(key)) {
                    elementMap.put(key, selector);
                    System.out.println("Added element: " + key + " with selector: " + selector);
                }
            } catch (Exception ignored) {
            }
        }

        return new JSONObject(elementMap);
    }

    private static String getReadableKey(WebElement element) {
        if (!element.getAttribute("type").isEmpty()) {
            return element.getTagName() + "_type_" + element.getAttribute("type");
        } else if (!element.getAttribute("data_element_type").isEmpty()) {
            return element.getTagName() + "_data_element_type" + element.getAttribute("data_element_type");
        } else if (!element.getAttribute("class").isEmpty()) {
            return element.getTagName() + "_class_" + element.getAttribute("class").split(" ")[0];
        }
        return null;
    }

    private static String getAccessibleCssSelector(WebElement element) {
        if (!element.getAttribute("type").isEmpty()) {
            return element.getTagName() + "[type='" + element.getAttribute("type") + "']";
        } else if (!element.getAttribute("data_element_type").isEmpty()) {
            return element.getTagName() + "[data_element_type='" + element.getAttribute("data_element_type") + "']";
        } else if (!element.getAttribute("class").isEmpty()) {
            return element.getTagName() + "." + element.getAttribute("class").split(" ")[0];
        } else {
            return element.getTagName();
        }
    }

    public static void exportToJsonFile(JSONObject jsonObject, String filename) {
        String outputPath = "src/main/resources/testData/" + filename;

        try {
            Files.createDirectories(Paths.get("src/main/resources/testData"));
            FileWriter file = new FileWriter(outputPath);
            file.write(jsonObject.toString(4));
            file.close();
            System.out.println("Page elements written to: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<String> urls = Arrays.asList("https://www.kripya.com/");
        WebDriver driver = new ChromeDriver();
        for (String url : urls) {
            driver.get(url);
            driver.manage().window().maximize();
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(driver1 -> ((JavascriptExecutor) driver1)
                            .executeScript("return document.readyState").equals("complete"));

            String extractedDomain = url.replace("https://", "").split("/")[0].split("\\.")[1];
                JSONObject pageElements = scanPage(driver);
                exportToJsonFile(pageElements, extractedDomain+".json");

        }
            driver.quit();
    }
}
