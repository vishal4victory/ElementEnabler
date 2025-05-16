package drivers;

import org.example.utils.FindResourceElement;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class SeleniumDriverFactory implements DriverFactory {

    WebDriver driver;
    FindResourceElement findRElement;
    int cartCount=0;

    public SeleniumDriverFactory() {
        driver = new ChromeDriver();

        driver.manage().window().maximize();
    }

    public void launch(String resource) {
        findRElement = new FindResourceElement(resource);
        String url = findRElement.json.getPrimaryLocator("url");
        System.out.println("Launching url: " + url);
        driver.get(url);
    }

    public void search(String searchText) {
        WebElement searchBox = getElement("input_search");
        searchBox.sendKeys(searchText + Keys.ENTER);
    }

    public void addFirstItemToCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> buttonAddtoCart = getElements("button_add_to_cart");
        WebElement firstItem = wait.until(ExpectedConditions.elementToBeClickable(buttonAddtoCart.get(0)));
        firstItem.click();
}

    public void addItemToCart(int resultItem) {
        List<WebElement> addToCartItems = getElements("button_add_to_cart");
        addToCartItems.get(resultItem).click();
    }

    @Override
    public void searchElementUpdate() {
        updateElementID("input_search");
    }

    public boolean isItemInCart() {
        String count = getElement("span_cart_count").getText();
        return count.equals(String.valueOf(cartCount));
    }

    public void quit() {
        if (driver != null)
            driver.quit();
    }

    public void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public WebElement getElement(String locatorKey) {
        return getElement(locatorKey, 0);
    }

    public WebElement getElement(String locatorKey, int index) {
        return (WebElement) findRElement.withValue(driver, locatorKey).get(index);
    }

    public List<WebElement> getElements(String locatorKey) {
        return (List<WebElement>) findRElement.withValue(driver, locatorKey);
    }

    public void updateElementID(String locatorKey){
        WebElement element = getElement(locatorKey);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].id = 'threetabsearchtextbox';", element);
    }
}
