package ca.mcgill.ecse428.jerseycabinet.acceptance.US021;

import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.service.discountService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.*;

@CucumberContextConfiguration
@SpringBootTest
public class DiscountStepDefinitions {

    @Autowired
    private JerseyRepository jerseyRepository;

    @Autowired
    private discountService discountService;

    private String errorMessage;
    private boolean discountApplied;

    @Given("the following jerseys exist:")
    public void the_following_jerseys_exist(List<List<String>> dataTable) {
        jerseyRepository.deleteAll();
        for (int i = 1; i < dataTable.size(); i++) {
            List<String> row = dataTable.get(i);
            Jersey jersey = new Jersey();
            jersey.setId(Integer.parseInt(row.get(0)));
            jersey.setSport(row.get(1));
            jersey.setSalePrice(Double.parseDouble(row.get(2)));
            jerseyRepository.save(jersey);
        }
    }

    @And("I am logged in as an Employee")
    public void i_am_logged_in_as_an_employee() {
        // Placeholder for authentication logic, you may need to mock this in actual tests.
    }

    @When("I apply a {int}% discount to all jerseys in the {string} category")
    public void i_apply_a_discount_to_all_jerseys_in_the_category(int discountPercentage, String category) {
        try {
            discountService.applyCategoryWideDiscount(category, discountPercentage);
            discountApplied = true;
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
            discountApplied = false;
        }
    }

    @When("I attempt to apply a {int}% discount to all jerseys in the {string} category")
    public void i_attempt_to_apply_a_discount_to_all_jerseys_in_the_category(int discountPercentage, String category) {
        try {
            discountService.applyCategoryWideDiscount(category, discountPercentage);
            discountApplied = true;
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
            discountApplied = false;
        }
    }

    @Then("the sale price of the jerseys in the {string} category should be updated:")
    public void the_sale_price_of_the_jerseys_in_the_category_should_be_updated(String category, List<List<String>> dataTable) {
        assertTrue(discountApplied);

        for (int i = 1; i < dataTable.size(); i++) {
            List<String> row = dataTable.get(i);
            int id = Integer.parseInt(row.get(0));
            double expectedPrice = Double.parseDouble(row.get(1));

            Jersey jersey = jerseyRepository.findJerseyById(id);
            assertNotNull(jersey);

            double actualPrice = jersey.getSalePrice(); // Get the actual price

            System.out.println("Jersey ID: " + id);
            System.out.println("Expected Price: " + expectedPrice);
            System.out.println("Actual Price: " + actualPrice);
            System.out.flush();

            assertEquals(expectedPrice, actualPrice, 0.01);
        }
    }

    @And("the sale price of jerseys in other categories should remain unchanged:")
    public void the_sale_price_of_jerseys_in_other_categories_should_remain_unchanged(List<List<String>> dataTable) {
        for (int i = 1; i < dataTable.size(); i++) {
            List<String> row = dataTable.get(i);
            int id = Integer.parseInt(row.get(0));
            double expectedPrice = Double.parseDouble(row.get(1));
            Jersey jersey = jerseyRepository.findJerseyById(id);
            assertNotNull(jersey);
            assertEquals(expectedPrice, jersey.getSalePrice(), 0.01);
        }
    }

    @Then("no jerseys should be updated")
    public void no_jerseys_should_be_updated() {
        assertFalse(discountApplied);
    }

    @And("an error message {string} should be displayed")
    public void an_error_message_should_be_displayed(String expectedMessage) {
        assertEquals(expectedMessage, errorMessage);
    }
}