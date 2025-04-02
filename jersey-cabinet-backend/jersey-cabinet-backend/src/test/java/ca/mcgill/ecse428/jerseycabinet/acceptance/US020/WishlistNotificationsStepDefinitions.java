package ca.mcgill.ecse428.jerseycabinet.acceptance.US020;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ca.mcgill.ecse428.jerseycabinet.service.NotificationService;
import ca.mcgill.ecse428.jerseycabinet.service.WishlistService;

@SpringBootTest
public class WishlistNotificationsStepDefinitions {

    @Autowired
    private WishlistService wishlistService;

    @MockBean
    private NotificationService notificationService;

    @Given("a customer with a wishlist containing jersey keywords")
    public void a_customer_with_a_wishlist_containing_jersey_keywords() {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("a jersey matching the wishlist criteria goes on sale")
    public void a_jersey_matching_the_wishlist_criteria_goes_on_sale() {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("the customer should receive a notification")
    public void the_customer_should_receive_a_notification() {
        // Write code here that turns the phrase above into concrete actions
    }
}
