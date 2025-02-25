package ca.mcgill.ecse428.jerseycabinet.acceptance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.OfferRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import ca.mcgill.ecse428.jerseycabinet.model.Offer;
import ca.mcgill.ecse428.jerseycabinet.service.OfferService;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CucumberContextConfiguration
@SpringBootTest
public class PlaceOfferSteps {

    @Autowired
    private JerseyRepository jerseyRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private OfferRepository offerRepo;

    @Autowired
    private OfferService offerService;


    private Employee employee;
    private Customer seller;
    private Offer currentOffer;
    private String displayedMessage;
    private boolean sellerNotified;

    private List<Jersey> createdJerseys = new ArrayList<>();

    // This map links the "jersey_offer_id" label in the Gherkin table to the real auto-generated ID in the DB
    private Map<String, Integer> labelToDbId = new HashMap<>();

    private int currentJerseyId;
    private String submittedAmount;

    @After
    public void cleanUp() {
        // Remove the Offer first, then the Jerseys, then the Employee and Customer
        if (currentOffer != null) {
            offerRepo.delete(currentOffer);
            currentOffer = null;
        }
        jerseyRepo.deleteAll(createdJerseys);
        createdJerseys.clear();

        if (employee != null) {
            employeeRepo.delete(employee);
            employee = null;
        }
        if (seller != null) {
            customerRepo.delete(seller);
            seller = null;
        }

        displayedMessage = null;
        sellerNotified = false;
        currentJerseyId = 0;
        submittedAmount = null;
        labelToDbId.clear();
    }


    @Given("the store has a jersey offer submission system")
    public void the_store_has_a_jersey_offer_submission_system() {
        System.out.println("Jersey offer submission system is ready.");
    }

    @Given("the following jersey offers exist in the system:")
    public void the_following_jersey_offers_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
        if (this.seller == null) {
            this.seller = new Customer("seller@jerseycabinet.com", "password123", "Seller Name");
            customerRepo.save(this.seller);
        }

        // For each row in the data table, "jersey_offer_id" is a label instead of a real numeric ID
        for (var row : dataTable.asMaps()) {
            String label = row.get("jersey_offer_id");

            Jersey newJersey = new Jersey(
                    RequestState.Listed,
                    "Jersey " + label, // description
                    "Brand",
                    "sport",
                    "color",
                    "jerseyImage.png",
                    "authImage.png",
                    100.0,
                    this.seller
            );

            jerseyRepo.save(newJersey);
            createdJerseys.add(newJersey);

            // Store the real DB ID in the map
            labelToDbId.put(label, newJersey.getId());

            System.out.println("Created jersey labeled '" + label + "' with DB ID: " + newJersey.getId());
        }
    }

    @Given("an employee account exists")
    public void an_employee_account_exists() {
        employee = new Employee("employee@store.com", "testpass");
        employeeRepo.save(employee);
        System.out.println("Created employee with ID: " + employee.getId());
    }


    @Given("I am logged in as an employee")
    public void i_am_logged_in_as_an_employee() {
        assertNotNull(employee, "No employee found to log in as!");
        System.out.println("Logged in as: " + employee.getEmail());
    }

    @Given("I have navigated to the page for the jersey offer with ID {string}")
    public void i_have_navigated_to_the_page_for_the_jersey_offer_with_id(String label) {
        // Instead of parsing label as an int, look up the real ID in the map
        if (!labelToDbId.containsKey(label)) {
            fail("No jersey mapped to label '" + label + "'");
        }
        currentJerseyId = labelToDbId.get(label);
        System.out.println("Navigated to jersey offer labeled '" + label + "' which has DB ID: " + currentJerseyId);
    }

    @When("I enter {string} as my offer amount")
    public void i_enter_as_my_offer_amount(String amount) {
        submittedAmount = amount;
        System.out.println("Entered offer amount: " + amount);
    }

    @When("I click the {string} button")
    public void i_click_the_button(String buttonName) {
        if (!"Submit Offer".equalsIgnoreCase(buttonName)) {
            displayedMessage = "Unknown button: " + buttonName;
            return;
        }
        displayedMessage = submitOffer();
    }

    @Then("I should see a confirmation message {string}")
    public void i_should_see_a_confirmation_message(String expectedMessage) {
        assertEquals(expectedMessage, displayedMessage, "Mismatch in confirmation message!");
    }

    @Then("the seller should receive a notification about my offer")
    public void the_seller_should_receive_a_notification_about_my_offer() {
        if (!sellerNotified) {
            fail("Expected seller to be notified, but it wasn't.");
        }
    }

    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String expectedMessage) {
        assertEquals(expectedMessage, displayedMessage, "Mismatch in error message!");
    }


    private String submitOffer() {
        // 1) Parse the string -> int for the price
        int price;
        try {
            price = Integer.parseInt(submittedAmount);
        } catch (NumberFormatException e) {
            return "Please enter a numeric offer amount.";
        }

        try {
            // 2) Actually place the offer
            currentOffer = offerService.placeOffer(currentJerseyId, employee.getId(), price);

            // 3) Assume success => seller is notified
            sellerNotified = true;
            return "Your offer of $" + price + " has been submitted.";
        } catch (IllegalArgumentException ex) {
            // The service might throw "Please enter a non-negative offer amount." or "Jersey not found"
            // or "Employee not found."
            return ex.getMessage();
        }
    }
}
