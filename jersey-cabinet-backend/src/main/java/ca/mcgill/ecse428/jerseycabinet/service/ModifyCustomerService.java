package ca.mcgill.ecse428.jerseycabinet.service;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModifyCustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Customer modifyCustomerAccount(int customerId, String newEmail, String newPassword, String newAddress) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Account not found.");
        }
        // If all fields are empty, throw an error.
        if ((newEmail == null || newEmail.isEmpty()) &&
                (newPassword == null || newPassword.isEmpty()) &&
                (newAddress == null || newAddress.isEmpty())) {
            throw new IllegalArgumentException("Please complete all required fields before submitting.");
        }
        if (newEmail != null && !newEmail.isEmpty()) {
            customer.setEmail(newEmail);
        }
        if (newPassword != null && !newPassword.isEmpty()) {
            customer.setPassword(newPassword);
        }
        if (newAddress != null && !newAddress.isEmpty()) {
            customer.setShippingAddress(newAddress);
        }
        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomerAccount(int customerId, String email, String password) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Account not found. Please check your details and try again.");
        }
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Email and password are required to delete your account.");
        }
        if (!customer.getEmail().equals(email) || !customer.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid credentials provided for deletion.");
        }
        customerRepository.delete(customer);
    }
}
