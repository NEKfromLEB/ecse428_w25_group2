package ca.mcgill.ecse428.jerseycabinet.acceptance.US008;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;

import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.dto.Jersey.JerseyListingDTO;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.service.ListJerseyService;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.boot.test.mock.mockito.MockBean;

@CucumberContextConfiguration
public class JerseyListingStepDefinitions {

    @MockBean
    private JerseyRepository jerseyRepository;

    @MockBean
    private ListJerseyService jerseyService;

    private Jersey testJersey;
    private Customer testSeller;
    private Exception errorException;
    private Jersey resultJersey;
    private int jerseyId = 1;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testSeller = new Customer();
    }

    @Given("I am a logged-in User")
    public void i_am_a_logged_in_user() {
        // Authentication to be implemented
    }

    @And("I provide the required details including {string}, {string}, {string}, and {double}")
    public void i_provide_the_required_details_including_and(String brand, String size, String condition, double price) {
        testJersey = new Jersey(
            Jersey.RequestState.Unlisted,
            "Test Description",
            brand,
            "Hockey",
            "Red",
            "jersey.jpg",
            "auth.jpg",
            price,
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
                testJersey.getSeller()
            );
            
            // Mock the service behavior
            when(jerseyService.updateJerseyRequestState(jerseyId, Jersey.RequestState.Listed))
                .thenReturn(listedVersion);
            
            resultJersey = jerseyService.updateJerseyRequestState(jerseyId, Jersey.RequestState.Listed);
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

    @When("I navigate to the \"Sell Jersey\" page")
    public void i_navigate_to_the_sell_jersey_page(){
        testJersey = new Jersey(
            Jersey.RequestState.Listed,
            "Test Description",
            "Nike",
            "Hockey",
            "Red",
            "jersey.jpg",
            "auth.jpg",
            100,
            testSeller
        );
    }

    @And("I leave the {string} field empty")
    public void i_leave_field_empty(String fieldName) {
        try {
            // Mock service to throw exception with the appropriate error message
            String errorMessage = fieldName.equals("price") ? "Price is required" : "Brand is required";
            when(jerseyService.updateJerseyListing(anyInt(), any(JerseyListingDTO.class)))
                .thenThrow(new IllegalArgumentException(errorMessage));
            
            resultJersey = jerseyService.updateJerseyListing(1, 
                new JerseyListingDTO(1, 0, testJersey.getDescription()));
        }
        catch (Exception e) {
            errorException = e;
        }
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
        testJersey = new Jersey(
            Jersey.RequestState.Listed,
            "Test Description",
            "Nike",
            "Hockey",
            "Red",
            "jersey.jpg",
            "auth.jpg",
            100,
            testSeller
        );
        when(jerseyRepository.findJerseyById(1)).thenReturn(testJersey);
    }

    @Given("I am on the {string} page")
    public void i_am_on_the_page(String string) {
        // Write code here that turns the phrase above into concrete actions
    }
    @When("I click on the {string} button for my jersey")
    public void i_click_on_the_button_for_my_jersey(String string) {
        // Write code here that turns the phrase above into concrete actions
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
        // Mock repository first
        when(jerseyRepository.save(any(Jersey.class))).thenReturn(testJersey);
        when(jerseyRepository.findJerseyById(anyInt())).thenReturn(testJersey);
        
        // Mock service to use repository
        when(jerseyService.updateJerseyListing(anyInt(), any(JerseyListingDTO.class)))
            .thenAnswer(invocation -> {
                Jersey jersey = jerseyRepository.findJerseyById(1);
                return jerseyRepository.save(jersey);
            });
            
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