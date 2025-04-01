package ca.mcgill.ecse428.jerseycabinet.service;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
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

}
