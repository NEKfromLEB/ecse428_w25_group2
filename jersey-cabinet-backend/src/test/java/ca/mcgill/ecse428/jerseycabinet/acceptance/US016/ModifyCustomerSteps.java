package ca.mcgill.ecse428.jerseycabinet.acceptance.US016;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.service.ModifyCustomerService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@CucumberContextConfiguration
@SpringBootTest
public class ModifyCustomerSteps {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModifyCustomerService modifyCustomerService;

    private Customer testCustomer;
    private Exception exception;

    private String originalEmail;
    private String originalPassword;
    private String originalAddress;

    private String enteredEmail;
    private String enteredPassword;
    private int testCustomerId;  // store the created customer's id

    private String partialEmail;
    private String partialPassword;
    private String partialAddress;

    @Given("the system has an account management platform")
    public void theSystemHasAnAccountManagementPlatform() {
        // Create a test customer.
        testCustomer = new Customer("customer@email.com", "Pass123", "123 Street St.");
        customerRepository.save(testCustomer);
        testCustomer = customerRepository.findCustomerById(testCustomer.getId());
        testCustomerId = testCustomer.getId();

        // Store the original values.
        originalEmail = testCustomer.getEmail();
        originalPassword = testCustomer.getPassword();
        originalAddress = testCustomer.getShippingAddress();
    }

    // ===== Normal Flow: Successful Modification =====
    @When("the customer updates their email to {string}, password to {string}, and shipping address to {string}")
    public void theCustomerUpdatesTheirEmailToPasswordToAndShippingAddressTo(String newEmail, String newPassword, String newAddress) {
        exception = null;
        try {
            modifyCustomerService.modifyCustomerAccount(testCustomer.getId(), newEmail, newPassword, newAddress);
        } catch (Exception e) {
            exception = e;
        }
        testCustomer = customerRepository.findCustomerById(testCustomer.getId());
    }

    @Then("the system updates their account information")
    public void theSystemUpdatesTheirAccountInformation() {
        assertNotNull("Customer should exist after update", testCustomer);
    }

    @And("the customer's information is successfully modified")
    public void theCustomerSInformationIsSuccessfullyModified() {
        assertNull("No exception should be thrown on successful modification", exception);
    }

    // ===== Normal Flow: Successful Deletion =====
    @When("they enter their email {string} and password {string}")
    public void theyEnterTheirEmailAndPassword(String email, String password) {
        if (!email.equals(testCustomer.getEmail()) || !password.equals(testCustomer.getPassword())) {
            try {
                testCustomer = modifyCustomerService.modifyCustomerAccount(
                        testCustomer.getId(),
                        email,
                        password,
                        testCustomer.getShippingAddress()
                );
            } catch (Exception e) {
                exception = e;
            }
        }

        enteredEmail = email;
        enteredPassword = password;
    }

    @And("they confirm account deletion")
    public void theyConfirmAccountDeletion() {
        try {
            modifyCustomerService.deleteCustomerAccount(testCustomer.getId(), enteredEmail, enteredPassword);
            testCustomer = null;
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the system permanently deletes their account")
    public void theSystemPermanentlyDeletesTheirAccount() {
        entityManager.clear();
        Customer deleted = customerRepository.findCustomerById(testCustomerId);
        assertNull("Customer should have been deleted", deleted);
    }

    // ===== Alternative Flow: Partial Update =====
    @When("the customer updates either their email to {string} or password to {string} or address to {string}")
    public void theCustomerUpdatesEitherTheirEmailToOrPasswordToOrAddressTo(String email, String password, String address) {
        partialEmail = email;
        partialPassword = password;
        partialAddress = address;
        exception = null;
        try {
            modifyCustomerService.modifyCustomerAccount(testCustomer.getId(), email, password, address);
        } catch (Exception e) {
            exception = e;
        }
        testCustomer = customerRepository.findCustomerById(testCustomer.getId());
    }

    @Then("the system updates only the provided information successfully")
    public void theSystemUpdatesOnlyTheProvidedInformationSuccessfully() {
        if (partialEmail != null && !partialEmail.isEmpty()) {
            assertEquals(partialEmail, testCustomer.getEmail());
        } else {
            assertEquals(originalEmail, testCustomer.getEmail());
        }
        if (partialPassword != null && !partialPassword.isEmpty()) {
            assertEquals(partialPassword, testCustomer.getPassword());
        } else {
            assertEquals(originalPassword, testCustomer.getPassword());
        }
        if (partialAddress != null && !partialAddress.isEmpty()) {
            assertEquals(partialAddress, testCustomer.getShippingAddress());
        } else {
            assertEquals(originalAddress, testCustomer.getShippingAddress());
        }
    }

    // ===== Error Flow: Incomplete Account Deletion =====
    @When("the client requests to delete their account with {string} and {string}")
    public void theClientRequestsToDeleteTheirAccountWithAnd(String email, String password) {
        enteredEmail = email;
        enteredPassword = password;
    }

    @Then("the system prevents deletion if any of those fields are empty")
    public void theSystemPreventsDeletionIfAnyOfThoseFieldsAreEmpty() {
        try {
            modifyCustomerService.deleteCustomerAccount(testCustomer.getId(), enteredEmail, enteredPassword);
            fail("Deletion should have been prevented due to empty fields.");
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        Customer existing = customerRepository.findCustomerById(testCustomer.getId());
        assertNotNull("Customer should still exist", existing);
    }

    @And("an error message is displayed: {string}")
    public void anErrorMessageIsDisplayed(String expectedMessage) {
        assertNotNull("An error message was expected", exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    // ===== Error Flow: Deletion of Non-Existent Account =====
    @When("the client requests to delete an account that does not exist with {string} and {string}")
    public void theClientRequestsToDeleteAnAccountThatDoesNotExistWithAnd(String email, String password) {
        enteredEmail = email;
        enteredPassword = password;
        exception = null;
        try {
            modifyCustomerService.deleteCustomerAccount(-1, enteredEmail, enteredPassword); // invalid id
            fail("Deletion of a non-existent account should fail.");
        } catch (IllegalArgumentException e) {
            exception = e;
        }
    }

    @Then("the system prevents deletion")
    public void theSystemPreventsDeletion() {
        assertNotNull("Expected deletion prevention", exception);
    }

    // ===== Error Flow: Update with All Fields Empty =====
    @When("the user tries to update their email to {string}, password to {string}, or shipping address to {string}")
    public void theUserTriesToUpdateTheirEmailToPasswordToOrShippingAddressTo(String email, String password, String address) {
        exception = null;
        try {
            modifyCustomerService.modifyCustomerAccount(testCustomer.getId(), email, password, address);
        } catch (Exception e) {
            exception = e;
        }
        testCustomer = customerRepository.findCustomerById(testCustomer.getId());
    }

    @And("all fields are empty")
    public void allFieldsAreEmpty() {
    }

    @Then("the system prevents the update")
    public void theSystemPreventsTheUpdate() {
        if (exception != null) {
            assertEquals("Please complete all required fields before submitting.", exception.getMessage());
        } else {
            assertEquals(originalEmail, testCustomer.getEmail());
            assertEquals(originalPassword, testCustomer.getPassword());
            assertEquals(originalAddress, testCustomer.getShippingAddress());
        }
    }
}
