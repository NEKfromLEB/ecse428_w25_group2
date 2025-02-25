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

    @Given("the store has an online checkout system")
    public void the_store_has_an_online_checkout_system() {
        paymentMethodRepository.deleteAll();
        customerRepository.deleteAll();
        customer = new Customer("customer@email.com", "password123", "123 Shipping St");
        customerRepository.save(customer);
    }

    @When("the user fills out the card information with card number {string}, expiration date {string}, CVV {string}, and cardholder name {string}")
    public void the_user_fills_out_card_information(String cardNumber, String expirationDate, String cvv, String cardholderName) {
        paymentDTO = new PaymentDTO();
        paymentDTO.setCardNumber(cardNumber);
        paymentDTO.setExpirationDate(expirationDate);
        paymentDTO.setCvv(cvv);
        paymentDTO.setCardHolderName(cardholderName);
        billingAddress = "123 Billing Street, City, State 12345";
    }

    @When("the user submits the payment form")
    public void the_user_submits_the_payment_form() {
        try {
            processedPayment = paymentService.processPayment(paymentDTO, billingAddress, customer);
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

    @When("any field is missing")
    public void any_field_is_missing() {
        paymentDTO.setCardNumber("");  // Simulate missing card number
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

    @When("the user enters invalid card information")
    public void the_user_enters_invalid_card_information() {
        paymentDTO.setExpirationDate("invalid-date");
    }

    @Then("the system prevents submission")
    public void the_system_prevents_submission() {
        assertNotNull(exception);
    }

    @Then("an error message is displayed: {string}")
    public void an_error_message_is_displayed(String errorMessage) {
        assertEquals(errorMessage, exception.getMessage());
    }

    @After
    public void tearDown() {
        paymentMethodRepository.deleteAll();
        customerRepository.deleteAll();
    }
} 