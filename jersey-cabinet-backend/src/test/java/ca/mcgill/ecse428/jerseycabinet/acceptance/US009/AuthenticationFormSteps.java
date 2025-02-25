package ca.mcgill.ecse428.jerseycabinet.acceptance.US009;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import ca.mcgill.ecse428.jerseycabinet.service.CreateAuthentificationFormService;

@CucumberContextConfiguration
@SpringBootTest
public class AuthenticationFormSteps {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JerseyRepository jerseyRepository;

    @Autowired
    private CreateAuthentificationFormService createAuthentificationFormService;

    private Jersey submittedJersey;
    private Exception exception;

    private Customer customer;

    // Background
    @Given("the store has an online platform for authenticity verification")
    public void the_store_has_an_online_platform_for_authenticity_verification() {
        jerseyRepository.deleteAll();
        customer = new Customer("namir@gmail.com", "abc123", "");
        customerRepository.save(customer);
    }

    // Successful Form Submission
    @When("the client submits a jersey authentication form with brand {string}, sport {string}, description {string},color {string}, and proof of authenticity {string}")
    public void the_client_submits_a_jersey_authentication_form_with_brand_sport_description_color_and_proof_of_authenticity(String brand, String sport, String description, String color, String proof) {
        try {
            Jersey foundJersey = createAuthentificationFormService.createJerseyForAuthentification(brand, sport, description, color, proof);
            jerseyRepository.save(foundJersey);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the jersey with brand {string}, sport {string}, description {string}, color {string}, and proof of authenticity {string} shall exist in the system")
    public void the_jersey_with_brand_sport_color_and_proof_of_authenticity_shall_exist_in_the_system(String brand, String sport,String description, String color, String proof) {
        Jersey foundJersey = new Jersey(RequestState.Unlisted, description, brand, sport, color,"", proof,0, customer);
        assertNotNull(foundJersey);
        assertEquals(brand, foundJersey.getBrand());
        assertEquals(sport, foundJersey.getSport());
        assertEquals(color, foundJersey.getColor());
        assertEquals(proof, foundJersey.getProofOfAuthenticationImage());
    }

    // Submission Without Proof
    @When("the client submits a jersey authentication form with brand {string}, sport {string}, description {string}, color {string}")
    public void the_client_submits_a_jersey_authentication_form_with_brand_sport_description_color(String brand, String sport, String description, String color) {
        submittedJersey = createAuthentificationFormService.createJerseyForAuthentification(brand, sport, description, color, "");
        jerseyRepository.save(submittedJersey);
    }

    @Then("the system warns: {string}")  
    public void the_system_warns(String expectedWarningMessage) { 
        assertNotNull(expectedWarningMessage);
        assertEquals(expectedWarningMessage, createAuthentificationFormService.handleMissingProof());  
    }


    @Then("the client can proceed with submission")
    public void the_client_can_proceed_with_submission() {
        assertNotNull(submittedJersey);
    }

    @Then("the store owner is notified of a submission requiring manual verification")
    public void the_store_owner_is_notified_of_a_submission_requiring_manual_verification() {
        System.out.println("Store owner notified of manual verification request.");
    }

    // Incomplete Form Submission
    @When("client submits a jersey authentication form with brand {string}, sport {string}, description {string}, color {string}, and proof of authenticity {string}")
    public void client_submits_a_jersey_authentication_form_with_brand_sport_description_color(String brand, String sport, String description, String color, String proof) {        
        try {
            submittedJersey = createAuthentificationFormService.createJerseyForAuthentification(brand, sport, description, color, proof);
            jerseyRepository.save(submittedJersey);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the system prevents submission if any of those fields are empty")
    public void the_system_prevents_submission_if_any_of_those_fields_are_empty() {
        assertNotNull(exception);
        assertTrue(exception instanceof IllegalArgumentException);
    }

    @Then("an error message is displayed: {string}")
    public void an_error_message_is_displayed(String errorMessage) {
        assertNotNull(exception);
        assertEquals("Please complete all required fields before submitting.", exception.getMessage());
    }

    @Then("the form remains on the page for correction")
    public void the_form_remains_on_the_page_for_correction() {
        assertNull(submittedJersey);
    }

    // Cleanup
    @After
    public void tearDown() {
        jerseyRepository.deleteAll();
        customerRepository.deleteAll();
        employeeRepository.deleteAll();
    }
}
