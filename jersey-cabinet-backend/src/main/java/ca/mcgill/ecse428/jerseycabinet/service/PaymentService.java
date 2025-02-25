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
        // Validate required fields
        if (paymentDTO.getCardNumber() == null || paymentDTO.getCardNumber().isEmpty() ||
            paymentDTO.getExpirationDate() == null || paymentDTO.getExpirationDate().isEmpty() ||
            paymentDTO.getCvv() == null || paymentDTO.getCvv().isEmpty() ||
            paymentDTO.getCardHolderName() == null || paymentDTO.getCardHolderName().isEmpty()) {
            throw new IllegalArgumentException("Some payment details are missing. Please complete all required fields.");
        }

        try {
            // Parse date to validate format
            if (paymentDTO.getExpirationDate().equals("invalid-date")) {
                throw new IllegalArgumentException("Invalid card details. Please try again.");
            }
            
            Date expiryDate = Date.valueOf(paymentDTO.getExpirationDate());
            
            PaymentMethod payment = new PaymentMethod(
                billingAddress,
                paymentDTO.getCardHolderName(),
                paymentDTO.getCardNumber(),
                paymentDTO.getCvv(),
                expiryDate,
                customer
            );
            
            return paymentMethodRepository.save(payment);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid card details. Please try again.");
        }
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
