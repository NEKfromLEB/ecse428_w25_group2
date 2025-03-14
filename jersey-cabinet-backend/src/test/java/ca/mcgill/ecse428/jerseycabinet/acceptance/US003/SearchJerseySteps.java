package ca.mcgill.ecse428.jerseycabinet.acceptance.US003;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import ca.mcgill.ecse428.jerseycabinet.service.SearchJerseyService;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
public class SearchJerseySteps {
    @Autowired
    private JerseyRepository jerseyRepo;
    @Autowired
    private EmployeeRepository employeeRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private SearchJerseyService searchJerseyService;

    private List<Jersey> searchResults;
    private String warningMessage;

    private Jersey jersey1;
    private Jersey jersey2;
    private Jersey jersey3;
    private Jersey jersey4;

    private Employee employee;
    private Customer customer;

    @After
    public void cleanUp(){
        jerseyRepo.delete(jersey1);
        jerseyRepo.delete(jersey2);
        jerseyRepo.delete(jersey3);
        jerseyRepo.delete(jersey4);
        employeeRepo.delete(employee);
        customerRepo.delete(customer);
    }

    @Given("the following listings exist in the system")
    public void the_following_listings_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
        employee = new Employee("emp@jerseycabinet.com", "pass123");
        customer = new Customer("mamhmoud@gmai.com", "abc123", "");

        employeeRepo.save(employee);
        customerRepo.save(customer);

        jersey1 = new Jersey(RequestState.Listed, "2002 Brazil Home", "", "soccer", "", "", "", 0, customer);

        jersey2 = new Jersey(RequestState.Listed, "2006 AC Milan Home", "", "soccer", "", "", "", 0, customer);

        jersey3 = new Jersey(RequestState.Listed, "1992 Chicago Bulls", "", "basketball", "", "", "", 0, customer);

        jersey4 = new Jersey(RequestState.Listed, "1984 Las Vegas Raiders", "", "football", "", "", "", 0, customer);

        jerseyRepo.save(jersey1);
        jerseyRepo.save(jersey2);
        jerseyRepo.save(jersey3);
        jerseyRepo.save(jersey4);
    }

    @Given("I am logged in as a user")
    public void i_am_logged_in_as_a_user() {
        //
    }

    @When("the user searches for {string}")
    public void the_user_searches_for(String searchQuery) {
        List<Jersey> allJerseys = searchJerseyService.findAllListedJerseys();
        List<Jersey> matchingJerseys = new ArrayList<>();

        for (Jersey jersey : allJerseys) {
            if (jersey.getDescription().toLowerCase().contains(searchQuery.toLowerCase())) {
                matchingJerseys.add(jersey);
            }
        }

        searchResults = matchingJerseys;
    }

    @Then("a list of {string} shall be displayed where each listing's description contains the search query as a substring")
    public void a_list_of_shall_be_displayed_where_each_listing_s_description_contains_the_search_query_as_a_substring(String expectedListings) {
        List<String> expectedDescriptions = Arrays.asList(expectedListings.split(", "));
        List<String> actualDescriptions = new ArrayList<>();
    
        for (Jersey jersey : searchResults) {
            actualDescriptions.add(jersey.getDescription());
        }
    
        assertEquals(expectedDescriptions, actualDescriptions);
    }

    @When("the user searches for listings indicating a {string}")
    public void the_user_searches_for_listings_indicating_a(String sport) {
        searchResults = searchJerseyService.findAllJerseysOfSport(sport);
    }

    @Then("a list of {string} of {string} shall be displayed")
    public void a_list_of_of_shall_be_displayed(String expectedListings, String sport) {
        List<String> expectedDescriptions = Arrays.asList(expectedListings.split(", "));
        List<String> actualDescriptions = new ArrayList<>();

        for (Jersey jersey : searchResults) {
            actualDescriptions.add(jersey.getDescription());
        }

        assertEquals(expectedDescriptions, actualDescriptions);
    }

    @Given("no listings match {string}")
    public void no_listings_match(String searchQuery) {
        List<Jersey> allJerseys = searchJerseyService.findAllListedJerseys();
    
        for (Jersey jersey : allJerseys) {
            if (jersey.getDescription().toLowerCase().contains(searchQuery.toLowerCase())) {
                fail("A listing matches the search query, but it should not.");
            }
        }
    }

    @Then("{string} is empty")
    public void is_empty(String listings) {
        assertTrue(searchResults.isEmpty());
    }

    @Then("the system warns the user that {int} matches were found")
    public void the_system_warns_the_user_that_matches_were_found(int count) {
        if (searchResults.isEmpty()) {
            warningMessage = "0 matches were found";
        } else {
            warningMessage = searchResults.size() + " matches were found";
        }

        assertEquals(count + " matches were found", warningMessage);
    }
}
