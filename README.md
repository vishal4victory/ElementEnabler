# 🚀 Demo Project: ElementEnabler

GitHub Repo: [https://github.com/vishal4victory/ElementEnabler](https://github.com/vishal4victory/ElementEnabler)

## 📄 Overview

ElementEnabler is a Java-based automation framework built with Cucumber, Selenium, and Playwright. It showcases advanced automation capabilities such as:

* UI Element Self-Healing
* Automated Object Repository Versioning
* Dynamic Test Data Generation
* Web Component Discovery
* Automated Script Generation from Functional Scenarios

---

## 1️⃣ UI Element Self-Healing

### Locator Management

* Page Object locators are stored externally in JSON format under `src/test/resources`. Example: [`amazon.json`](https://github.com/vishal4victory/ElementEnabler/blob/main/src/test/resources/amazon.json).

### Self-Healing Logic

* Each UI element includes:

  * A `primary` locator
  * Multiple `fallbacks`
* When a locator fails:

  1. Adjusts relative XPath if DOM structure has shifted
  2. Replaces strict text matching with contains logic
  3. Applies similar logic for CSS selectors
  4. Falls back to secondary locators
* If a fallback succeeds, the `primary` locator is updated dynamically, and the primary locator of the PO JSON file is updated(Self-healed). 

### Demo

* View test case: [search\_add\_cart.feature#L12](https://github.com/vishal4victory/ElementEnabler/blob/main/src/test/java/features/search_add_cart.feature#L12)
* Logic breakdown: [SeleniumDriverFactory.java#L82](https://github.com/vishal4victory/ElementEnabler/blob/main/src/test/java/drivers/SeleniumDriverFactory.java#L82)
* Self Heal, Retry, FailOver to FallBack: [FindResourceElement.java#L28](https://github.com/vishal4victory/ElementEnabler/blob/main/src/main/java/org/example/utils/FindResourceElement.java#L28)
* Console Output:
```
Launching url: https://www.amazon.in
  Given I open the Amazon homepage          # stepdefinitions.SearchAndAddSteps.openAmazon()
Element locator: //input[@id='twotabsearchtextbox']
WebElement Size: 1
  Then modify the search element id         # stepdefinitions.SearchAndAddSteps.modifyTheSearchElementId()
Element locator: //input[@id='twotabsearchtextbox']
Element not found, trying to modify the locator
Element not found even after modifying the locator
Element locator: //span[text()='TEXT_VALUE']
Element not found, trying to modify the locator
Element not found even after modifying the locator
Element locator: //button[@class='btn-login']
Element not found, trying to modify the locator
Element not found even after modifying the locator
Element locator: //form[@id='nav-search-bar-form']/input
Element not found, trying to modify the locator
Updated jsonObject: {"url":{"primary":"https://www.amazon.in","fallbacks":["https://www.amazon.com"]},"button_add_to_cart":{"primary":"button[aria-label='Add to cart']","fallbacks":["","",""]},"span_cart_count":{"primary":"#nav-cart-count","fallbacks":["","",""]},"input_search":{"fallbacks":["//span[text()='TEXT_VALUE']","//button[@class='btn-login']","//form[@id='nav-search-bar-form']/input"],"primary":"//form[@id='nav-search-bar-form']//input"}}
Resource found at: /Users/sureshc/testRepo/ElementEnabler/target/test-classes/amazon.json
Updated locator: //form[@id='nav-search-bar-form']//input for field: input_search
CHanges updated on the PO-ElementFilesrc/test/resources/amazon.json
WebElement Size: 2
  When I search for "Toy Car"               # stepdefinitions.SearchAndAddSteps.search(java.lang.String)
Element locator: button[aria-label='Add to cart']
WebElement Size: 59
  And I add the first item to the cart      # stepdefinitions.SearchAndAddSteps.addToCart()
Element locator: #nav-cart-count
WebElement Size: 1
  Then the cart should contain the item     # stepdefinitions.SearchAndAddSteps.validateCart()
  
```
### Alternate Method (Optional)

* Java Reflection can modify Page Object fields dynamically, but it's slower and more fragile.

---

## 2️⃣ Object Repository Auto-Versioning & PR Creation

### Purpose

Automatically commit and version locator changes after test execution

### Workflow (CI/CD-ready)

* Detect modified locator files after test
* Create a branch: `auto/heal-<timestamp>`
* Add and commit changes
* Push the branch
* Create a pull request via GitHub CLI

### Example Shell Logic

```bash
git status --porcelain src/test/resources/ > modified_files.txt
git checkout -b auto/heal-$(date +%s)
git add <modified files>
git commit -m "Auto-healed locators for: <files>"
git push --set-upstream origin <branch>
gh pr create --base master --head <branch> \
  --title "Auto-healed locators" \
  --body "Updated locator(s) after fallback recovery"
```

> Can be integrated to Jenkin PIpeline as a Step in the JenkinsFile to trigger after "test" execution.
> Also, can include a toggle to disable auto-PR creation if needed, just in case these element failovers are a defect that needs to be addressed. 

---

## 3️⃣ Automated Test Data Generation

* [GenerateUserPayload](https://github.com/vishal4victory/ElementEnabler/blob/main/src/main/java/org/example/utils/GenerateUserPayload.java#L12)
Uses Java Faker to create realistic, dynamic test data in JSON format.

### Features

* Name, email, phone, password, and address fields
* Pretty-printed JSON
* Output stored in `src/main/resources/testdata`
* Timestamped filenames for tracking

### Example Output

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "123-456-7890",
  "password": "Secure@123"
}
```

---

## 4️⃣ Automated Component Discovery

This utility scans a webpage, identifies UI elements, and saves them in organized JSON files.
* [POGenerator](https://github.com/vishal4victory/ElementEnabler/blob/main/src/main/java/org/example/utils/POGenerator.java)

### Benefits

* Bootstraps Page Object creation
* Reduces manual locator capture
* Keeps locators centralized and maintainable

---

## 5️⃣ Automated Script Generation from Functional Tests

### Strategy

* Use Gherkin Feature files as input
* Generate step definitions via IDE plugins
* Leverage AI (VSCode, IntelliJ plugins) to suggest implementation

### Recommended Practices

* Modularize actions like navigation, login, form submission
* Reuse across pages to reduce redundancy

---

## 📅 Summary

| Feature                           | Status  |
| --------------------------------- | ------- |
| UI Element Self-Healing           | ✅       |
| Dynamic Locator Repository        | ✅       |
| PR Automation for Healed Locators | ✅       |
| Test Data Generator (JSON Output) | ✅       |
| UI Component Scanner              | ✅       |
| Script Generation from Gherkin    | 🔹 Beta |

---
## Steps to Run
## Prerequisites
To set up and use ElementEnabler, ensure your system meets the following requirements:
- Java Development Kit (JDK) 24 or later.
- Maven 3.9 or later.
- Browsers installed for driving tests (e.g., Chrome, Firefox, etc.).
- Internet connection for resolving project dependencies.

## Installation
1. Clone the repository:
``` bash
   git clone <repository-url>
```
1. Navigate to the project folder:
``` bash
   cd ElementEnabler
```
1. Build the project and download dependencies:
``` bash
   mvn clean install
```
## Usage
1. **Run Selenium Tests** - Create and execute Selenium-based automation tests.
2. **Execute BDD Scenarios** - Write feature files and run them using Cucumber.
3. **Generate Test Data** - Utilize Java Faker to mock realistic test data.
4. **Perform Advanced Browser Automation** - Use Playwright for testing modern web applications across different browsers.
5. **Logging** - Check logs generated by Logback for analyzing test or application behavior.

### Running Tests
- **TestNG tests**:
``` bash
   mvn clean test
```

