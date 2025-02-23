package ca.mcgill.ecse428.jerseycabinet.acceptance;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


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
    private Jersey jersey2;
    private Jersey jersey3;

    private Employee employee;
    private Customer customer;
    ArrayList<Jersey> unlisted_jerseys;

    @After
    public void cleanup(){
        jerseyRepo.delete(jersey1);
        jerseyRepo.delete(jersey2);
        jerseyRepo.delete(jersey3);
        employeeRepo.delete(employee);
        customerRepo.delete(customer);
    }

  
    @Given("the following authentication requests exist in the system")
    public void the_following_authentication_requests_exist_in_the_system(io.cucumber.datatable.DataTable dataTable){
        customer = new Customer("tee@gmail.com","hehe123","123 Main Street");

        jersey1 = new Jersey(null,"This is a jersey.","Nike","soccer","black",null,null,null,customer);
        jersey2 = new Jersey(null,"A jersey this is.","Adidas","basketball","blue",null,null,null,customer);
        jersey3 = new Jersey(null,"Is this a jersey?","Puma","football","orange",null,null,null,customer);

        customerRepo.save(customer);
        jerseyRepo.save(jersey1);
        jerseyRepo.save(jersey2);
        jerseyRepo.save(jersey3);
    }

    @And("I am logged in as an employee")
    public void i_am_logged_in_as_an_employee(){
        employee = new Employee("a@jerseycabinet.com","jersey123");
        employeeRepo.save(employee);
    }

    @And("I am viewing the list of pending requests")
    public void i_am_viewing_the_list_of_pending_requests(){
        this.unlisted_jerseys = authenService.findAllUnreviewedJerseys();
    }

    @When("I confirm the authencity of the jersey {int}")
    public void i_confirm_the_authencity_of_the_jersey(int id) throws Exception{
        authenService.findJerseyById(id);
        authenService.acceptRequestById(id,employee);
    }

    @Then("the jersey {int} is marked as unlisted")
    public void the_jersey_is_marked_as_unlisted(int id) throws Exception{
        Jersey jersey = authenService.findJerseyById(id);
        assertEquals(RequestState.Unlisted,jersey.getRequestState());
    }

    @And("the jersey {int} is assigned to me.")
    public void the_jersey_is_assigned_to_the_employee_who_made_the_decision(int id) throws Exception{
        Jersey jersey = authenService.findJerseyById(id);
        assertEquals(employee,jersey.getEmployee());
    }

    @And("I reject the authencity of the jersey {int}")
        public void i_reject_the_authencity_of_the_jersey(int id) throws Exception{
            authenService.findJerseyById(id);
            authenService.rejectRequestById(id,employee);
        }
    

    @Then("the jersey {int} is marked as rejected")
    public void the_jersey_is_marked_as_rejected(int id) throws Exception{
        Jersey jersey = authenService.findJerseyById(id);
        assertEquals(RequestState.Rejected,jersey.getRequestState());
    }


    @When("I view a request {int} but do not make a decision ")
    public void i_request_to_view_but_do_not_make_a_decision(int id) throws Exception{
        authenService.findJerseyById(id);
    }

    @Then("the jersey {int} status remains unchanged")
    public void the_jersey_status_remains_unchanged(int id) throws Exception{
        Jersey jersey = authenService.findJerseyById(id);
        assertNull(jersey.getRequestState());
    }
    
    @And("the jersey {int} remains in the list of requests to be reviewed")
    public void the_jersey_remains_in_the_list_of_requests_to_be_reviewed(int id){


    }
    
   

}




