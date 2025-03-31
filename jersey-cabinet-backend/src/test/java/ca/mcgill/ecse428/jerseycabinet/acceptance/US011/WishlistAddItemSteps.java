package ca.mcgill.ecse428.jerseycabinet.acceptance.US011;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.WishlistRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Wishlist;
import ca.mcgill.ecse428.jerseycabinet.service.CustomerAddJerseyToWishlist;
import ca.mcgill.ecse428.jerseycabinet.service.CustomerLoginSignUpService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
public class WishlistAddItemSteps {

    @Autowired
    private CustomerAddJerseyToWishlist wishlistService;

    @Autowired
    private CustomerLoginSignUpService customerService;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;
    private Wishlist wishlist;


    @Given("the customer is logged into their account")
    public void theUserIsOnTheRegistrationPage() {
        wishlistRepository.deleteAll();
        customerRepository.deleteAll();
        wishlist = null;
        customer = customerService.registerCustomer("example@gmail.com", "David1234", "example address");
        // Simulate user being on the registration page
    }

    @When("the customer requests to add a jersey to their wishlist")
    public void theCustomerRequestsToAddAJerseyToTheirWishlist(){
        wishlist = wishlistService.getWishlistByCustomerEmail(customer.getEmail());
        assertNotNull(wishlist);

        wishlist = wishlistService.addJerseyToWishlist(wishlist.getId(), "red, H&M");
    }

    @Then("the jersey is added to the customer's wishlist")
    public void theJerseyIsAddedToTheCustomersWishlist() {
        assertEquals("red, H&M, ", wishlist.getKeywords());
    }

    @When("the customer requests to see their wishlist")
    public void theCustomerRequestsToSeeTheirWishlist() {
        wishlist = wishlistService.getWishlistByCustomerEmail(customer.getEmail());
        assertNotNull(wishlist);
    }

    @Then("the wishlist displays all previously added jerseys")
    public void theWishlistDisplaysAllPreviouslyAddedJerseys() {
        assertEquals(null, wishlist.getKeywords());
    }
}