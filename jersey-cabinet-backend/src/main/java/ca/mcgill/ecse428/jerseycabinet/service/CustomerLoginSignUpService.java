package ca.mcgill.ecse428.jerseycabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.WishlistRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Wishlist;

@Service
public class CustomerLoginSignUpService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private WishlistRepository wishlistRepository;

    public Customer registerCustomer(String email, String password, String shippingAddress) {
        if (customerRepository.findCustomerByEmail(email) != null) {
            throw new IllegalArgumentException("Email already in use.");
        }

        if(password == null || password.length() < 7) {
            throw new IllegalArgumentException("Password must be at least 7 characters long");
        }

        if(shippingAddress == null || shippingAddress.length() == 0) {
            throw new IllegalArgumentException("shipping address must be valid");
        }

        int i = email.indexOf("@");
        if (i == -1 || i == 0 || i == email.length() - 1) {
            throw new IllegalArgumentException("email must contain an @");
        }

        if(!(email.chars().filter(ch -> ch == '@').count() == 1)) {
            throw new IllegalArgumentException("email must contain only 1 @");
        }
        i = email.indexOf(".com");
        if (i <= 0 || i != email.length() - 4) {
            throw new IllegalArgumentException("email must contain .com at the end");
        }

        if(!email.strip().equals(email)) {
            throw new IllegalArgumentException("Email must not contain spaces");
        }

        // altered code to create a wishlist for the new user
        Customer newCustomer = new Customer(email, password, shippingAddress);
        newCustomer = customerRepository.save(newCustomer);

        try {
            Wishlist wishlist = new Wishlist(null, newCustomer);
            wishlistRepository.save(wishlist);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to create wishlist: " + e.getMessage());
        }

        return newCustomer;
    }

    public Customer loginCustomer(String email, String password) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null || !customer.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        return customer;
    }

    public Customer getCustomerById(int id) {
        Customer customer = customerRepository.findCustomerById(id);

        if(customer == null) throw new IllegalArgumentException("Id Does not Exist!");

        return customer;
    }
}
