package ca.mcgill.ecse428.jerseycabinet.acceptance.US013;

import static org.junit.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.OfferRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import ca.mcgill.ecse428.jerseycabinet.model.Offer;
import ca.mcgill.ecse428.jerseycabinet.model.Offer.OfferState;
import ca.mcgill.ecse428.jerseycabinet.service.OfferService;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
public class ReviewOfferSteps {
    @Autowired
    private OfferRepository offerRepo;
    @Autowired
    private JerseyRepository jerseyRepo;
    @Autowired
    private EmployeeRepository employeeRepo;
    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private OfferService service;

    private Employee employee;
    private Jersey jersey1;
    private Jersey jersey2;
    private Jersey jersey3;
    private Customer customer;
    private Offer offer1;
    private Offer offer2;
    private Offer offer3;

    private String errorMessage;

    @After
    public void cleanUp(){
        offerRepo.delete(offer1);
        offerRepo.delete(offer2);
        offerRepo.delete(offer3);
        jerseyRepo.delete(jersey1);
        jerseyRepo.delete(jersey2);
        jerseyRepo.delete(jersey3);
        employeeRepo.delete(employee);
        customerRepo.delete(customer);
    }

    @Given("the following jerseys exist in the system")
    public void the_following_jerseys_exist_in_the_system(io.cucumber.datatable.DataTable dataTable){
        employee = new Employee("emp@jerseycabinet.com", "pass123");
        customer = new Customer("mahmoud@gmai.com", "abc123", "");

        employeeRepo.save(employee);
        customerRepo.save(customer);

        jersey1 = new Jersey(RequestState.Unlisted, "This is a jersey.", "Nike", "soccer", "black", "", "", 0, customer);
        jersey2 = new Jersey(RequestState.Unlisted, "This is a jersey.", "Adidas", "soccer", "black", "", "", 0, customer);
        jersey3 = new Jersey(RequestState.Listed, "This is a jersey.", "Adidas", "soccer", "black", "", "", 0, customer);

        jerseyRepo.save(jersey1);
        jerseyRepo.save(jersey2);
        jerseyRepo.save(jersey3);
    }

    @Given("the following offers exist in the system")
    public void the_following_offers_exist_in_the_system(io.cucumber.datatable.DataTable dataTable){
        Offer.OfferKey key1 = new Offer.OfferKey(jersey1, employee);
        offer1 = new Offer(key1, OfferState.Pending, 100);

        Offer.OfferKey key2 = new Offer.OfferKey(jersey2, employee);
        offer2 = new Offer(key2, OfferState.Pending, 150);

        Offer.OfferKey key3 = new Offer.OfferKey(jersey3, employee);
        offer3 = new Offer(key3, OfferState.Pending, 100);

        offerRepo.save(offer1);
        offerRepo.save(offer2);
        offerRepo.save(offer3);
    }

    @Given("I am logged in as a Customer")
    public void i_am_logged_in_as_a_customer() {
        //
    }

    @When("I confirm and accept the offer")
    public void i_confirm_and_accept_the_offer(){
        offer1 = service.acceptOffer(jersey1.getId(), employee.getId());
    }

    @Then("the offer state becomes accepted")
    public void the_offer_state_becomes_accepted(){
        assertEquals(OfferState.Accepted, offer1.getOfferState());
    }

    @Then("the jersey state becomes listed")
    public void the_jersey_state_becomes_listed(){
        jersey1 = jerseyRepo.findJerseyById(jersey1.getId());
        assertEquals(RequestState.Listed, jersey1.getRequestState());
    }

    @When("I confirm and reject the offer")
    public void i_confirm_and_reject_the_offer(){
        offer2 = service.rejectOffer(jersey2.getId(), employee.getId());
    }

    @Then("the offer state becomes rejected")
    public void the_offer_state_becomes_rejected(){
        assertEquals(OfferState.Rejected, offer2.getOfferState());
    }

    @Then("the jersey state remains unlisted")
    public void the_jersey_state_remains_unlisted(){
        jersey2 = jerseyRepo.findJerseyById(jersey2.getId());
        assertEquals(RequestState.Unlisted, jersey2.getRequestState());
    }

    @When("I attempt to accept the offer")
    public void i_attempt_to_accept_the_offer(){
        try{
            offer3 = service.acceptOffer(jersey3.getId(), employee.getId());
        }
        catch(IllegalArgumentException e){
            errorMessage = e.getMessage();
        }
    }

    @Then("the system warns the user that an offer has been previously accepted for this jersey")
    public void the_system_warns_the_user_that_an_offer_has_been_previously_accepted_for_this_jersey(){
        assertEquals(errorMessage, "An offer has been previously accepted for this jersey.");
    }

    @Then("the offer state remains unchanged")
    public void the_offer_state_remains_unchanged(){
        assertEquals(OfferState.Pending, offer3.getOfferState());
    }

    @Then("the jersey state remains unchanged")
    public void the_jersey_state_remains_unchanged(){
        jersey3 = jerseyRepo.findJerseyById(jersey3.getId());
        assertEquals(RequestState.Listed, jersey3.getRequestState());
    }  
}
