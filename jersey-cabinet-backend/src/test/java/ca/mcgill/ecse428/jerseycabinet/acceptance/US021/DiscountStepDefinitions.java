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
import org.springframework.boot.test.context.TestConfiguration;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
            jersey.setSport(row.get(0));
            jersey.setSalePrice(Double.parseDouble(row.get(1)));
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
        List<Jersey> jerseys = jerseyRepository.findBySport(category);
        List<Jersey> jerseyList = jerseys.stream().collect(Collectors.toList());
        assertEquals(dataTable.size() - 1, jerseyList.size());

        for (int i = 1; i < dataTable.size(); i++) {
            List<String> row = dataTable.get(i);
            double expectedSalePrice = Double.parseDouble(row.get(0));

            boolean found = false;
            for (Jersey jersey : jerseyList) {
                if (jersey.getSalePrice() == expectedSalePrice) {
                    found = true;
                    break;
                }
            }
            assertTrue("Jersey with sale price " + expectedSalePrice + " not found", found);
        }
    }

    @Then("the sale price of jerseys in other categories should remain unchanged:")
    public void the_sale_price_of_jerseys_in_other_categories_should_remain_unchanged(List<List<String>> dataTable) {
        for (int i = 1; i < dataTable.size(); i++) {
            List<String> row = dataTable.get(i);
            String sport = row.get(0);
            double expectedSalePrice = Double.parseDouble(row.get(1));

            List<Jersey> jerseys = jerseyRepository.findBySport(sport);

            boolean found = false;
            for(Jersey jersey : jerseys){
                if(jersey.getSalePrice() == expectedSalePrice){
                    found = true;
                    break;
                }
            }
            assertTrue("Jersey with sport: "+ sport + " and sale price: "+ expectedSalePrice + " not found.", found);
        }
    }

    @Then("no jerseys should be updated")
    public void no_jerseys_should_be_updated() {
        List<Jersey> allJerseys = new ArrayList<>();
        jerseyRepository.findAll().forEach(allJerseys::add);
        assertTrue(allJerseys.size() > 0);
        assertFalse(discountApplied);
    }

    @Then("an error message {string} should be displayed")
    public void an_error_message_should_be_displayed(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, errorMessage);
    }

    @TestConfiguration
    static class TestConfig {
        // Optional: Add custom beans or configurations here
    }
}