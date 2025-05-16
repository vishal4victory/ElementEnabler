package drivers;

public interface DriverFactory {
    void launch(String url);
    void search(String text);
    void addFirstItemToCart();
    boolean isItemInCart();
    void quit();
    void wait(int seconds);
    void addItemToCart(int resultItem);
    void searchElementUpdate();
}
