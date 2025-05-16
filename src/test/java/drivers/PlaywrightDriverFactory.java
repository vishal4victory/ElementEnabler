package drivers;

import com.microsoft.playwright.*;
import org.example.utils.FindResourceElement;

public class PlaywrightDriverFactory implements DriverFactory {

    Playwright playwright;
    Browser browser;
    Page page;
    FindResourceElement findRElement;

    public PlaywrightDriverFactory() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    public void launch(String resource) {
        findRElement = new FindResourceElement(resource);
        String url = findRElement.json.getPrimaryLocator("url");
        System.out.println("Launching url: " + url);
        page.navigate(url);
    }

    public void search(String text) {
        page.fill("#twotabsearchtextbox", text);
        page.keyboard().press("Enter");
        page.waitForLoadState();
    }

    public void addFirstItemToCart() {
        page.locator("button[aria-label='Add to cart']").first().click();
        page.waitForLoadState();
    }

    @Override
    public void addItemToCart(int resultItem) {
        page.locator("#add-to-cart-button").nth(resultItem).click();
    }

    @Override
    public void searchElementUpdate() {

    }

    public boolean isItemInCart() {
        String count = page.locator("#nav-cart-count").textContent();
        return !count.trim().equals("0");
    }

    public void quit() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    public void wait(int seconds) {
        try {
            page.wait(seconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
