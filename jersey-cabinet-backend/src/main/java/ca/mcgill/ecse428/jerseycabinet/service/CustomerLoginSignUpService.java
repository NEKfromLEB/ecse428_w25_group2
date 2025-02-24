package ca.mcgill.ecse428.jerseycabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;

@Service
public class CustomerLoginSignUpService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer registerCustomer(String email, String password, String shippingAddress) {
        if (!customerRepository.findCustomerByEmail(email).equals(null)) {
            throw new IllegalArgumentException("Email already in use.");
        }

        if(password.equals(null) || password.length() < 7) {
            throw new IllegalArgumentException("Password must be at least 7 characters long");
        }

        if(shippingAddress.equals(null) || shippingAddress.length() == 0) {
            throw new IllegalArgumentException("shipping address must be valid");
        }

        Customer newCustomer = new Customer(email, password, shippingAddress);
        
        return customerRepository.save(newCustomer);
    }

    public Customer loginCustomer(String email, String password) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer.equals(null) || !customer.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        return customer;
    }

    public Customer getCustomerById(int id) {
        Customer customer = customerRepository.findCustomerById(id);

        if(customer.equals(null)) throw new IllegalArgumentException("Id Does not Exist!");

        return customer;
    }
}
