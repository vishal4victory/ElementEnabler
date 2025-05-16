Feature: Search and Add to Cart

  Scenario: Search for Toy Car and add first result to cart
    Given I open the Amazon homepage
    When I search for "Toy Car"
    And I add the first item to the cart
    Then the cart should contain the item


  Scenario: Test to Validate UI Self Heal Fix
    Given I open the Amazon homepage
    Then modify the search element id
    When I search for "Toy Car"
    And I add the first item to the cart
    Then the cart should contain the item
