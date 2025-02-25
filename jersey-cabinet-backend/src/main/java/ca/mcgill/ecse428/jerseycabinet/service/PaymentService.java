package ca.mcgill.ecse428.jerseycabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ca.mcgill.ecse428.jerseycabinet.dao.PaymentMethodRepository;
import ca.mcgill.ecse428.jerseycabinet.model.PaymentMethod;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.dto.PaymentDTO;
import java.sql.Date;

@Service
public class PaymentService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    
    /**
     * Processes a payment using the provided PaymentDTO along with additional
     * billing address and customer information.
     *
     * @param paymentDTO the DTO containing payment details (card number, holder name, expiration date, cvv)
     * @param billingAddress the billing address for the payment
     * @param customer the Customer making the payment
     * @return the persisted PaymentMethod entity
     * @throws IllegalArgumentException if any required field is missing or invalid, or if payment processing fails
     */
    @Transactional
    public PaymentMethod processPayment(PaymentDTO paymentDTO, String billingAddress, Customer customer) {
        // Validate input parameters
        if (billingAddress == null || billingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Billing address is required.");
        }
        if (paymentDTO == null) {
            throw new IllegalArgumentException("Payment details must be provided.");
        }
        if (paymentDTO.getCardNumber() == null || paymentDTO.getCardNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Card number is required.");
        }
        if (paymentDTO.getCardHolderName() == null || paymentDTO.getCardHolderName().trim().isEmpty()) {
            throw new IllegalArgumentException("Card holder name is required.");
        }
        if (paymentDTO.getExpirationDate() == null || paymentDTO.getExpirationDate().trim().isEmpty()) {
            throw new IllegalArgumentException("Expiration date is required.");
        }
        if (paymentDTO.getCvv() == null || paymentDTO.getCvv().trim().isEmpty()) {
            throw new IllegalArgumentException("CVV is required.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer must be provided.");
        }
        
        // Simulate payment processing (replace with real API integration as needed)
        boolean paymentSuccess = simulatePaymentProcessing(paymentDTO);
        if (!paymentSuccess) {
            throw new IllegalArgumentException("Payment processing failed.");
        }
        
        // Convert the expiration date from String to java.sql.Date.
        // The expected format is "yyyy-[m]m-[d]d" (e.g., "2025-12-31").
        Date expiryDate;
        try {
            expiryDate = Date.valueOf(paymentDTO.getExpirationDate());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid expiration date format. Use yyyy-[m]m-[d]d.");
        }
        
        // Create the PaymentMethod entity using the provided details.
        PaymentMethod paymentMethod = new PaymentMethod(
            billingAddress,
            paymentDTO.getCardHolderName(),  // maps to cardName in PaymentMethod
            paymentDTO.getCardNumber(),
            paymentDTO.getCvv(),
            expiryDate,
            customer
        );
        
        // Persist the PaymentMethod entity in the database.
        paymentMethodRepository.save(paymentMethod);
        return paymentMethod;
    }
    
    /**
     * Simulates external payment processing.
     * In a real implementation, replace this with an API call (e.g., to Stripe or PayPal).
     *
     * @param paymentDTO the payment details.
     * @return true if the payment simulation is successful.
     */
    private boolean simulatePaymentProcessing(PaymentDTO paymentDTO) {
        // For simulation purposes, assume the payment always succeeds.
        return true;
    }
}
