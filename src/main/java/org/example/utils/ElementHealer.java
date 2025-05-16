package org.example.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.json.JSONArray;

public class ElementHealer {


    static final int defaultIndex = 0;
    int elementIndex = 0;

    public void setElementIndex(int elementIndex) {
        this.elementIndex = elementIndex;
    }

    // Try primary and fallback locators
    public static WebElement findElementWithFallbacks(WebDriver driver, String primary, JSONArray fallbacks) {
        WebElement element = tryFind(driver, primary);
        if (element != null) return element;

        for (int i = 0; i < fallbacks.length(); i++) {
            element = tryFind(driver, fallbacks.getString(i));
            if (element != null) return element;
        }
        return null;
    }

    public static WebElement tryFind(WebDriver driver, String value) {
        try {
            WebElement el = null;
            if (value.startsWith("//")) {
                el = driver.findElement(By.xpath(value));
            } else {
                el = driver.findElement(By.cssSelector(value));
            }

            if (el != null && el.isDisplayed()) {
                System.out.println("Found element with locator: " + value);
                return el;
            }
        } catch (NoSuchElementException e) {
            // If not found, try modifying the condition and retrying
            String modifiedSelector = parseAndModifyCondition(value);
            try {
                WebElement el = driver.findElement(By.xpath(modifiedSelector));
                if (el.isDisplayed()) {
                    System.out.println("Found element with modified locator: " + modifiedSelector);
                    return el;
                }
            } catch (NoSuchElementException ignored) {
            }
        }
        return null;
    }

    private static String parseAndModifyCondition(String xpath) {
        if (xpath.contains("text()")) {

            String[] parts = xpath.split("text\\(\\)");
            return xpath.replace("=", "contains").replace("\"", ",'").replace("']", "']");
        }
        return xpath;
    }
}
