package ca.mcgill.ecse428.jerseycabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ca.mcgill.ecse428.jerseycabinet.dao.PaymentMethodRepository;
import ca.mcgill.ecse428.jerseycabinet.model.PaymentMethod;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import java.sql.Date;

@Service
public class PaymentService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    
    /**
     * Processes a payment with the provided payment details
     *
     * @param cardNumber the card number
     * @param cardHolderName the name on the card
     * @param expirationDate the card expiration date
     * @param cvv the card CVV
     * @param billingAddress the billing address
     * @param customer the Customer making the payment
     * @return the persisted PaymentMethod entity
     * @throws IllegalArgumentException if any required field is missing or invalid
     */
    
    @Transactional
    public PaymentMethod processPayment(String cardNumber, String cardHolderName, 
            String expirationDate, String cvv, String billingAddress, Customer customer) {
        
        if (cardNumber == null || cardNumber.isEmpty() ||
            expirationDate == null || expirationDate.isEmpty() ||
            cvv == null || cvv.isEmpty() ||
            cardHolderName == null || cardHolderName.isEmpty()) {
            throw new IllegalArgumentException("Some payment details are missing. Please complete all required fields.");
        }

        try {
            if (expirationDate.equals("invalid-date")) {
                throw new IllegalArgumentException("Invalid card details. Please try again.");
            }
            
            Date expiryDate = Date.valueOf(expirationDate);
            
            PaymentMethod payment = new PaymentMethod();
            payment.setBillingAddress(billingAddress);
            payment.setCardName(cardHolderName);
            payment.setCardNumber(cardNumber);
            payment.setCardCVV(cvv);
            payment.setCardExpiryDate(expiryDate);
            payment.setCustomer(customer);

            paymentMethodRepository.save(payment);
            return payment;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid card details. Please try again.");
        }
    }
}
