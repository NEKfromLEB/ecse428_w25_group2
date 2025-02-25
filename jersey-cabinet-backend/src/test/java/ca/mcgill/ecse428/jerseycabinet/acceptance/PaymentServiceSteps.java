package ca.mcgill.ecse428.jerseycabinet.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.PaymentMethodRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.PaymentMethod;
import ca.mcgill.ecse428.jerseycabinet.dto.PaymentDTO;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import ca.mcgill.ecse428.jerseycabinet.service.PaymentService;

@CucumberContextConfiguration
@SpringBootTest
public class PaymentServiceSteps {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    private Customer customer;
    private PaymentDTO paymentDTO;
    private String billingAddress;
    private PaymentMethod processedPayment;
    private Exception exception;

    // Background
    @Given("the store has an online checkout system")
    public void the_store_has_an_online_checkout_system() {
        paymentMethodRepository.deleteAll();
        customerRepository.deleteAll();
        customer = new Customer("customer@email.com", "password123", "123 Shipping St");
        customerRepository.save(customer);
    }

    @Given("a user has added a jersey to their cart")
    public void a_user_has_added_a_jersey_to_their_cart() {
        assertNotNull(customer);
    }

    // Normal Flow
    @When("the user fills out the card information with card number {string}, expiration date {string}, CVV {string}, and cardholder name {string}")
    public void the_user_fills_out_card_information(String cardNumber, String expirationDate, String cvv, String cardholderName) {
        paymentDTO = new PaymentDTO();
        paymentDTO.setCardNumber(cardNumber);
        paymentDTO.setExpirationDate(formatDate(expirationDate));
        paymentDTO.setCvv(cvv);
        paymentDTO.setCardHolderName(cardholderName);
        billingAddress = "123 Test Street, Test City, TS 12345";
    }

    @When("the user submits the payment form")
    public void the_user_submits_the_payment_form() {
        try {
            processedPayment = paymentService.processPayment(
                paymentDTO.getCardNumber(),
                paymentDTO.getCardHolderName(),
                paymentDTO.getExpirationDate(),
                paymentDTO.getCvv(),
                billingAddress,
                customer
            );
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the system shall confirm the payment and display a success message")
    public void the_system_shall_confirm_the_payment() {
        assertNotNull(processedPayment);
        assertEquals(paymentDTO.getCardNumber(), processedPayment.getCardNumber());
        assertEquals(paymentDTO.getCardHolderName(), processedPayment.getCardName());
    }

    @Then("the order shall be recorded in the system")
    public void the_order_shall_be_recorded_in_the_system() {
        assertNotNull(processedPayment);
        assertNotNull(processedPayment.getId());
    }

    // Alternative Flow
    @When("any field is missing")
    public void any_field_is_missing() {
        submitPayment();
    }

    @Then("the system warns: {string}")
    public void the_system_warns(String warningMessage) {
        assertNotNull(exception);
        assertEquals(warningMessage, exception.getMessage());
    }

    @Then("the user is unable to proceed with the payment")
    public void the_user_is_unable_to_proceed_with_payment() {
        assertNull(processedPayment);
    }

    // Error Flow
    @When("the user enters invalid card information with card number {string}, expiration date {string}, CVV {string}, or cardholder name {string}")
    public void the_user_enters_invalid_card_information(String cardNumber, String expirationDate, String cvv, String cardholderName) {
        paymentDTO = new PaymentDTO();
        paymentDTO.setCardNumber(cardNumber);
        paymentDTO.setExpirationDate("invalid-date");
        paymentDTO.setCvv(cvv);
        paymentDTO.setCardHolderName(cardholderName);
        billingAddress = "123 Test Street, Test City, TS 12345";
        submitPayment();
    }

    @Then("the system prevents submission")
    public void the_system_prevents_submission() {
        assertNotNull(exception, "Expected an exception but none was thrown");
    }

    @Then("an error message is displayed: {string}")
    public void an_error_message_is_displayed(String errorMessage) {
        assertNotNull(exception, "Expected an exception but none was thrown");
        assertEquals(errorMessage, exception.getMessage());
    }

    @Then("the form remains on the page for correction")
    public void the_form_remains_on_the_page_for_correction() {
        assertNull(processedPayment);
        assertNotNull(exception);
    }

    // Helper Methods
    private String formatDate(String date) {
        try {
            String[] parts = date.split("/");
            String month = parts[0];
            String year = "20" + parts[1];
            return year + "-" + month + "-01";
        } catch (Exception e) {
            return "";
        }
    }

    private void submitPayment() {
        try {
            processedPayment = paymentService.processPayment(
                paymentDTO.getCardNumber(),
                paymentDTO.getCardHolderName(),
                paymentDTO.getExpirationDate(),
                paymentDTO.getCvv(),
                billingAddress,
                customer
            );
        } catch (Exception e) {
            exception = e;
        }
    }

    // Cleanup
    @After
    public void tearDown() {
        paymentMethodRepository.deleteAll();
        customerRepository.deleteAll();
    }
} 