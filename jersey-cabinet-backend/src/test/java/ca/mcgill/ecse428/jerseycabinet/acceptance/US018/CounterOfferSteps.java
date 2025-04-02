package ca.mcgill.ecse428.jerseycabinet.acceptance.US018;

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
public class CounterOfferSteps {
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
   private Customer customer;
   private Offer offer1;
   private Offer offer2;

   private int newPrice;
   private String errorMessage;

   @After
   public void cleanUp(){
      offerRepo.delete(offer1);
      offerRepo.delete(offer2);
      jerseyRepo.delete(jersey1);
      jerseyRepo.delete(jersey2);
      employeeRepo.delete(employee);
      customerRepo.delete(customer);
   }

   @Given("the following offers exist in the system")
   public void the_following_offers_exist_in_the_system(io.cucumber.datatable.DataTable dataTable){
      employee = new Employee("emp@jerseycabinet.com", "pass123");
      customer = new Customer("mahmoud@gmai.com", "abc123", "");

      employeeRepo.save(employee);
      customerRepo.save(customer);

      jersey1 = new Jersey(RequestState.Unlisted, "This is a jersey.", "Nike", "soccer", "black", "", "", 0, customer);
      jersey2 = new Jersey(RequestState.Unlisted, "This is a jersey.", "Adidas", "soccer", "black", "", "", 0, customer);

      jerseyRepo.save(jersey1);
      jerseyRepo.save(jersey2);

      Offer.OfferKey key1 = new Offer.OfferKey(jersey1, employee);
      offer1 = new Offer(key1, OfferState.Rejected, 100);

      Offer.OfferKey key2 = new Offer.OfferKey(jersey2, employee);
      offer2 = new Offer(key2, OfferState.Accepted, 150);

      offerRepo.save(offer1);
      offerRepo.save(offer2);
   }

   @Given("I am logged in as an Employee")
   public void i_am_logged_in_as_an_employee() {
        //
   }

   @When("I enter a {int} for the offer")
   public void i_enter_a_for_the_offer(int price){
      newPrice = price;
   }

   @When("I confirm the update of the offer")
   public void i_confirm_the_update_of_the_offer(){
      try{
         offer1 = service.counterOffer(jersey1.getId(), employee.getId(), newPrice);
      }
      catch(IllegalArgumentException e){
         errorMessage = e.getMessage();
      }
   }

   @Then("the offer state becomes pending")
   public void the_offer_state_becomes_pending(){
      assertEquals(offer1.getOfferState(), OfferState.Pending);
   }

   @Then("the price is updated to the new price")
   public void the_price_is_updated_to_the_new_price(){
      assertEquals(newPrice, offer1.getPrice());
   }

   @When("I attempt to counter an accepted offer")
   public void i_attempt_to_counter_an_accepted_offer(){
      try{
         offer2 = service.counterOffer(jersey2.getId(), employee.getId(), newPrice);
      }
      catch(IllegalArgumentException e){
         errorMessage = e.getMessage();
      }
   }

   @Then("the system warns the user that accepted offers cannot be modified")
   public void the_system_warns_the_user_that_accepted_offers_cannot_be_modified(){
      assertEquals("Accepted offers cannot be modified.", errorMessage);
   }

   @Then("the offer state remains accepted")
   public void the_offer_state_reamins_accepted(){
      assertEquals(OfferState.Accepted, offer2.getOfferState());
   }

   @When("I enter a negative {int} for the offer")
   public void i_enter_a_negative_for_the_offer(int price){
      newPrice = price;
   }

   @Then("the system warns the user to enter a non-negative offer amount")
   public void the_system_warns_the_user_to_enter_a_non_negative_offer_amount() {
      assertEquals("Please enter a non-negative offer amount.", errorMessage);
   }

   @Then("the offer state remains unchanged")
   public void the_offer_state_remains_unchanged(){
      assertEquals(OfferState.Rejected, offer1.getOfferState());
   }
}
