package ca.mcgill.ecse428.jerseycabinet.acceptance;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.model.*;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import ca.mcgill.ecse428.jerseycabinet.service.AuthenticationService;

import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@CucumberContextConfiguration
@SpringBootTest
public class AuthenticationStepDefinitions {
    @Autowired
    private JerseyRepository jerseyRepo;
    @Autowired 
    private EmployeeRepository employeeRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private AuthenticationService authenService;

    private Jersey jersey1;
    private int id;


    private Employee employee;
    private Customer customer;
    ArrayList<Jersey> unlisted_jerseys;
 
    
    @After
    public void cleanup(){
        jerseyRepo.delete(this.jersey1);
        employeeRepo.delete(this.employee);
        customerRepo.delete(this.customer);
    }

  
    @Given("the following authentication request exists in the system")
    public void the_following_authentication_requests_exist_in_the_system(io.cucumber.datatable.DataTable dataTable){
        customer = new Customer("tee@gmail.com","hehe123","123 Main Street");

        this.jersey1 = new Jersey(null,"This is a jersey.","Nike","soccer","black",null,null,customer);
        customerRepo.save(this.customer);
        jerseyRepo.save(this.jersey1);
        this.id = this.jersey1.getId();
    }

    @And("I am logged in as an Employee")
    public void i_am_logged_in_as_an_employee(){
        this.employee = new Employee("a@jerseycabinet.com","jersey123");
        employeeRepo.save(this.employee);
    }

    @And("I am viewing the list of pending requests")
    public void i_am_viewing_the_list_of_pending_requests(){
        this.unlisted_jerseys = authenService.findAllUnreviewedJerseys();
    }

    @When("I confirm the authencity of the jersey")
    public void i_confirm_the_authencity_of_the_jersey() throws Exception{
        authenService.findJerseyById(this.id);
        authenService.acceptRequestById(this.id,this.employee);
    }

    @Then("the jersey is marked as unlisted")
    public void the_jersey_is_marked_as_unlisted() throws Exception{
        Jersey jersey = authenService.findJerseyById(this.id);
        assertEquals(RequestState.Unlisted,jersey.getRequestState());
    }

    @And("the jersey is assigned to me.")
    public void the_jersey_is_assigned_to_the_employee_who_made_the_decision() throws Exception{
        Jersey jersey = authenService.findJerseyById(this.id);
        assertEquals(employee.getId(),jersey.getEmployee().getId());
    }

    @When("I reject the authencity of the jersey")
        public void i_reject_the_authencity_of_the_jersey() throws Exception{
            authenService.findJerseyById(this.id);
            authenService.rejectRequestById(this.id,this.employee);
        }
    

    @Then("the jersey is marked as rejected")
    public void the_jersey_is_marked_as_rejected() throws Exception{
        Jersey jersey = authenService.findJerseyById(this.id);
        assertEquals(RequestState.Rejected,jersey.getRequestState());
    }


    @When("I view a request but do not make a decision")
    public void i_request_to_view_but_do_not_make_a_decision() throws Exception{
        authenService.findJerseyById(this.id);
    }

    @Then("the jersey status remains unchanged")
    public void the_jersey_status_remains_unchanged() throws Exception{
        Jersey jersey = authenService.findJerseyById(this.id);
        assertNull(jersey.getRequestState());
    }
    
    @And("the jersey remains in the list of requests to be reviewed.")
    public void the_jersey_remains_in_the_list_of_requests_to_be_reviewed(){
        assertNotNull(authenService.findAllUnreviewedJerseys());
        assertEquals(authenService.findAllUnreviewedJerseys().get(1).getId(),jersey1.getId());
    }
    
   

}




