package org.example.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FindResourceElement {

    String elementFile;
    public JsonParserUtil json;
    String path;


    public FindResourceElement(String elementFile) {
        this.elementFile = elementFile;
        this.json = new JsonParserUtil(elementFile);
        this.path = "src/test/resources/" + elementFile;
    }

    public List<WebElement> withValue(WebDriver driver, String fName) {

        List<String> elements = getElements(driver, fName);
        List<WebElement> el = null;
        for (String elementLocator: elements) {
            System.out.println("Element locator: " + elementLocator);
            if(elementLocator==null) continue;

            boolean elementUpdated = false;
            el = findElementsByXpathOrCss(driver, elementLocator);
            if (el.isEmpty()) {
                System.out.println("Element not found, trying to modify the locator");
                elementLocator = parseAndModifyCondition(elementLocator);
                el = findElementsByXpathOrCss(driver, elementLocator);
                elementUpdated=true;
            }
            if(el.isEmpty()){
                System.out.println("Element not found even after modifying the locator");
                continue;
            }
                if (elementUpdated) {
                    json.setLocatorOnObject(fName, elementLocator);
                    saveUpdatedJson();
                    System.out.println("Updated locator: " + elementLocator + " for field: " + fName);
                    System.out.println("CHanges updated on the PO-ElementFile" + path);
                }
                System.out.println("WebElement Size: " + el.size());
                return el;

        }
        return null;
    }

    private List<WebElement> findElementsByXpathOrCss(WebDriver driver, String locator) {

        List<WebElement> el = null;
        if (locator.startsWith("//")) {
            el = driver.findElements(By.xpath(locator));
        } else {
            el = driver.findElements(By.cssSelector(locator));
        }
        return el;
    }


    private static String parseAndModifyCondition(String value) {
        if (value.startsWith("//")) {
            // Handles
            if (value.contains("text()")) {
                value =  value.replaceAll("\\[text\\(\\)='(.*?)'\\]", "[contains(text(),'$1')]");
            }
            value = value.replaceAll("(?<!/)/(?!/)", "//");
        }else {
            value = value.replaceAll("=", "*=");
        }
        return value;
    }

    private List<String> getElements(WebDriver driver, String fName) {
        List<String> elements = new ArrayList<String>();
        elements.add(json.getPrimaryLocator(fName));
        int elementIndex = 0;
       while(json.getfallbacksLocator(fName, elementIndex) != null){
           elements.add(json.getfallbacksLocator(fName, elementIndex));
           elementIndex++;
       }
        return elements;
    }

    private void saveUpdatedJson() {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(this.elementFile);
        if (resource == null) {
            System.out.println("Unable to find resource: " + this.elementFile);
        } else {
            System.out.println("Resource found at: " + resource.getPath());
        }


        try (FileWriter file = new FileWriter(path)) {
            Gson gson = new GsonBuilder().setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            String prettyJson = gson.toJson(json.jsonObject);
            file.write(prettyJson);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
