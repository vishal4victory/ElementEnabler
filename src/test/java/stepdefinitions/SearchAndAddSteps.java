package stepdefinitions;

import actions.WebActions;
import drivers.DriverFactory;
import drivers.SeleniumDriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.en.*;

import static org.junit.Assert.*;

public class SearchAndAddSteps {

    DriverFactory driver = WebActions.getFactory();

    @Given("I open the Amazon homepage")
    public void openAmazon() throws InterruptedException {
        driver.launch("amazon.json");
    }

    @When("I search for {string}")
    public void search(String item) {
        driver.search(item);
    }

    @When("I add the first item to the cart")
    public void addToCart() {
        driver.addFirstItemToCart();
    }

    @Then("the cart should contain the item")
    public void validateCart() {
        assertTrue(driver.isItemInCart());
    }

    @After
    public void tearDown() {
        WebActions.reset();
    }

    @Then("modify the search element id")
    public void modifyTheSearchElementId() {
        driver.searchElementUpdate();
    }
}
