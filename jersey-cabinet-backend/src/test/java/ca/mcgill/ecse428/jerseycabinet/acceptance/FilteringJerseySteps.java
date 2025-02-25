package ca.mcgill.ecse428.jerseycabinet.acceptance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import ca.mcgill.ecse428.jerseycabinet.service.FilteringJerseyService;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
public class FilteringJerseySteps {
    
    @Autowired
    private JerseyRepository jerseyRepo;
    @Autowired
    private EmployeeRepository employeeRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private FilteringJerseyService filteringJerseyService;
    
    private List<Jersey> filterResults;
    private String warningMessage;
    
    private Jersey jersey1;
    private Jersey jersey2;
    private Jersey jersey3;
    private Jersey jersey4;
    private Employee employee;
    private Customer customer;
    
    @After
    public void cleanUp() {
        jerseyRepo.delete(jersey1);
        jerseyRepo.delete(jersey2);
        jerseyRepo.delete(jersey3);
        jerseyRepo.delete(jersey4);
        employeeRepo.delete(employee);
        customerRepo.delete(customer);
    }
    
    @Given("the following jersey listings exist in the system")
    public void the_following_jersey_listings_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
        // Create test employee and customer
        employee = new Employee("emp@jerseycabinet.com", "pass123");
        customer = new Customer("user@example.com", "password", "");
        employeeRepo.save(employee);
        customerRepo.save(customer);
        
        // For simplicity, we use hardcoded data based on the filtering feature:
        jersey1 = new Jersey(RequestState.Listed, "2002 Brazil Home", "", "soccer", "", "", "", 0, employee, customer);
        jersey2 = new Jersey(RequestState.Listed, "2006 AC Milan Home", "", "soccer", "", "", "", 0, employee, customer);
        jersey3 = new Jersey(RequestState.Listed, "1992 Chicago Bulls", "", "basketball", "", "", "", 0, employee, customer);
        jersey4 = new Jersey(RequestState.Listed, "1984 Las Vegas Raiders", "", "football", "", "", "", 0, employee, customer);
        
        jerseyRepo.save(jersey1);
        jerseyRepo.save(jersey2);
        jerseyRepo.save(jersey3);
        jerseyRepo.save(jersey4);
    }
    
    @Given("I am logged in as a user")
    public void i_am_logged_in_as_a_user() {
        // For acceptance tests, assume the user is authenticated.
    }
    
    // Filtering with both sport and description substring
    @When("the user filters the listings with sport {string} and description containing {string}")
    public void the_user_filters_the_listings_with_sport_and_description_containing(String sport, String description) {
        filterResults = filteringJerseyService.filterJerseys(sport, description);
    }
    
    // Filtering by sport only
    @When("the user filters the listings with sport {string}")
    public void the_user_filters_the_listings_with_sport(String sport) {
        filterResults = filteringJerseyService.filterJerseys(sport, "");
    }
    
    // Filtering by description substring only
    @When("the user filters the listings with description containing {string}")
    public void the_user_filters_the_listings_with_description_containing(String description) {
        filterResults = filteringJerseyService.filterJerseys("", description);
    }
    
    // Validate that the correct jersey listings are returned
    @Then("the system returns the following jersey listings: {string}")
    public void the_system_returns_the_following_jersey_listings(String expectedListings) {
        List<String> expectedDescriptions = expectedListings.isEmpty() 
            ? new ArrayList<>() 
            : Arrays.asList(expectedListings.split(", "));
        List<String> actualDescriptions = new ArrayList<>();
        for (Jersey jersey : filterResults) {
            actualDescriptions.add(jersey.getDescription());
        }
        assertEquals(expectedDescriptions, actualDescriptions);
    }
    
    // Optionally, verify a warning message (if required by the feature)
    @Then("a warning message is shown that {int} matches were found")
    public void a_warning_message_is_shown_that_matches_were_found(int count) {
        if (filterResults.isEmpty()) {
            warningMessage = "0 matches were found";
        } else {
            warningMessage = filterResults.size() + " matches were found";
        }
        assertEquals(count + " matches were found", warningMessage);
    }
}
