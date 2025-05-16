package actions;

import config.Config;
import drivers.*;

public class WebActions {
    private static DriverFactory factory;

    public static DriverFactory getFactory() {
        if (factory == null) {
            factory = Config.DRIVER.equalsIgnoreCase("playwright")
                    ? new PlaywrightDriverFactory()
                    : new SeleniumDriverFactory();
        }
        return factory;
    }

    public static void reset() {
        if (factory != null) {
            factory.quit();
            factory = null;
        }
    }
}
