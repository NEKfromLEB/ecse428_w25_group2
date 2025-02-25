package ca.mcgill.ecse428.jerseycabinet.acceptance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.dto.Jersey.JerseyListingDTO;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import ca.mcgill.ecse428.jerseycabinet.service.ListJerseyService;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest
public class JerseyListingStepDefinitions {

    @Mock
    private JerseyRepository jerseyRepository;

    @InjectMocks
    private ListJerseyService jerseyService;

    private Jersey testJersey;
    private Customer testSeller;
    private Employee testEmployee;
    private Exception errorException;
    private Jersey resultJersey;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testSeller = new Customer();
        testEmployee = new Employee();
    }

    @Given("I am a logged-in User")
    public void i_am_a_logged_in_user() {
        // Authentication to be implemented
    }

    @When("I navigate to the {string} page")
    public void i_navigate_to_the_page(String pageName) {
        // Navigation to be implemented in frontend
    }

    @And("I provide the required details including brand {string}, size {string}, condition {string}, and price {double}")
    public void i_provide_required_details(String brand, String size, String condition, double price) {
        testJersey = new Jersey(
            Jersey.RequestState.Unlisted,
            "Test Description",
            brand,
            "Hockey",
            "Red",
            "jersey.jpg",
            "auth.jpg",
            price,
            testEmployee,
            testSeller
        );
        when(jerseyRepository.findJerseyById(1)).thenReturn(testJersey);
    }

    @And("I upload a clear image of the jersey")
    public void i_upload_clear_image() {
        testJersey.setJerseyImage("jersey.jpg");
    }

    @And("I click on the {string} button")
    public void i_click_button(String buttonName) {
        try {
            Jersey listedVersion = new Jersey(
                Jersey.RequestState.Listed,
                testJersey.getDescription(),
                testJersey.getBrand(),
                testJersey.getSport(),
                testJersey.getColor(),
                testJersey.getJerseyImage(),
                testJersey.getProofOfAuthenticationImage(),
                testJersey.getSalePrice(),
                testJersey.getEmployee(),
                testJersey.getSeller()
            );
            when(jerseyRepository.updateJerseyRequestState(1, Jersey.RequestState.Listed))
                .thenReturn(listedVersion);
            
            resultJersey = jerseyService.updateJerseyRequestState(1, Jersey.RequestState.Listed);
        } catch (Exception e) {
            errorException = e;
        }
    }

    @Then("my jersey should be successfully listed for sale")
    public void jersey_should_be_listed() {
        assertNotNull(resultJersey);
        assertEquals(Jersey.RequestState.Listed, resultJersey.getRequestState());
    }

    @And("other Users should be able to view it in the marketplace")
    public void jersey_should_be_visible() {
        assertTrue(resultJersey.getRequestState() == Jersey.RequestState.Listed);
    }

    @And("I leave the {string} field empty")
    public void i_leave_field_empty(String fieldName) {
        if (fieldName.equals("price")) {
            testJersey.setSalePrice(0.0);
        } else if (fieldName.equals("brand")) {
            testJersey.setBrand(null);
        }
        when(jerseyRepository.findJerseyById(1)).thenReturn(testJersey);
    }

    @Then("I should see an error message {string}")
    public void i_should_see_error(String errorMessage) {
        assertNotNull(errorException);
        assertTrue(errorException.getMessage().contains(errorMessage));
    }

    @And("my jersey should not be listed for sale")
    public void jersey_should_not_be_listed() {
        verify(jerseyRepository, never()).save(any(Jersey.class));
    }

    @Given("I have already listed a jersey for sale")
    public void i_have_listed_jersey() {
        testJersey.setRequestState(Jersey.RequestState.Listed);
        when(jerseyRepository.findJerseyById(1)).thenReturn(testJersey);
    }

    @When("I update the {string} to {string}")
    public void i_update_field(String field, String value) {
        if (field.equals("price")) {
            double newPrice = Double.parseDouble(value);
            testJersey.setSalePrice(newPrice);
        } else if (field.equals("condition")) {
            // Condition update logic to be implemented
        }
        when(jerseyRepository.save(any(Jersey.class))).thenReturn(testJersey);
    }

    @And("I click on {string}")
    public void i_click_on(String buttonName) {
        resultJersey = jerseyService.updateJerseyListing(1, 
            new JerseyListingDTO(1, testJersey.getSalePrice(), testJersey.getDescription()));
    }

    @Then("my jersey listing should be updated successfully")
    public void jersey_should_be_updated() {
        assertNotNull(resultJersey);
        verify(jerseyRepository).save(any(Jersey.class));
    }

    @And("the updated details should be visible to potential buyers")
    public void updated_details_should_be_visible() {
        assertTrue(resultJersey.getRequestState() == Jersey.RequestState.Listed);
    }
}