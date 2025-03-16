package ca.mcgill.ecse428.jerseycabinet.acceptance.US015;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import ca.mcgill.ecse428.jerseycabinet.service.ModifyListingService;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
public class ModifyListingsServiceSteps {
    @Autowired
    private JerseyRepository jerseyRepo;
    @Autowired 
    private ModifyListingService listService;
    @Autowired 
    private EmployeeRepository employeeRepo;
    @Autowired
    private CustomerRepository customerRepo;

    private Employee employee;
    private Customer customer;
    private Jersey jersey1;
    private Jersey jersey2;
    private int id;
    private Exception prev_exception;

     @After
    public void cleanup(){
        jerseyRepo.delete(this.jersey1);
        employeeRepo.delete(this.employee);
        customerRepo.delete(this.customer);
        this.prev_exception = null;
    }
    
    @Given("I am logged in as an Employee")
    public void I_am_logged_in_as_an_Employee(){
        this.employee = new Employee("a@jerseycabinet.com","jersey123");
        employeeRepo.save(this.employee);
    }

    @And("the following listed jersey is selected")
    public void the_following_listed_jersey_is_selected(io.cucumber.datatable.DataTable dataTable){
        customer = new Customer("tee@gmail.com","hehe123","123 Main Street");
        this.jersey1 = new Jersey(RequestState.Listed,"This is a jersey.","Nike","soccer","black","google", "www.drive.com", 65,customer);
        this.jersey1.setEmployee(this.employee);
        customerRepo.save(this.customer);
        jerseyRepo.save(this.jersey1);
        this.id = this.jersey1.getId();
    }
    @When("I modify a {string} with {string}")
    public void i_modify_a_with(String field, String info) throws Exception{
        if (field.equals("description")){
            listService.modifyById(this.id,info,jersey1.getBrand(),jersey1.getSport(),jersey1.getColor(),jersey1.getJerseyImage(),jersey1.getSalePrice(),jersey1.getProofOfAuthenticationImage());
            this.jersey2 = new Jersey(RequestState.Listed,info,"Nike","soccer","black","google", "www.drive.com", 65,customer);
        }else if (field.equals("brand")){
            listService.modifyById(this.id,jersey1.getDescription(),info,jersey1.getSport(),jersey1.getColor(),jersey1.getJerseyImage(),jersey1.getSalePrice(),jersey1.getProofOfAuthenticationImage());
            this.jersey2 = new Jersey(RequestState.Listed,"This is a jersey.",info,"soccer","black","google", "www.drive.com", 65,customer);
        }else if(field.equals("sport")){
            listService.modifyById(this.id,jersey1.getDescription(),jersey1.getBrand(),info,jersey1.getColor(),jersey1.getJerseyImage(),jersey1.getSalePrice(),jersey1.getProofOfAuthenticationImage());
            this.jersey2 = new Jersey(jersey1.getRequestState(),jersey1.getDescription(),jersey1.getBrand(),info,jersey1.getColor(),jersey1.getJerseyImage(),jersey1.getProofOfAuthenticationImage(),jersey1.getSalePrice(),customer);
        }
        else if (field.equals("color")){
            listService.modifyById(this.id,jersey1.getDescription(),jersey1.getBrand(),jersey1.getSport(),info,jersey1.getJerseyImage(),jersey1.getSalePrice(),jersey1.getProofOfAuthenticationImage());
            this.jersey2 = new Jersey(jersey1.getRequestState(),jersey1.getDescription(),jersey1.getBrand(),jersey1.getSport(),info,jersey1.getJerseyImage(),jersey1.getProofOfAuthenticationImage(),jersey1.getSalePrice(),customer);
        }else if (field.equals("jerseyImage")){
            listService.modifyById(this.id,jersey1.getDescription(),jersey1.getBrand(),jersey1.getSport(),jersey1.getColor(),info,jersey1.getSalePrice(),jersey1.getProofOfAuthenticationImage());
            this.jersey2 = new Jersey(jersey1.getRequestState(),jersey1.getDescription(),jersey1.getBrand(),jersey1.getSport(),jersey1.getColor(),info,jersey1.getProofOfAuthenticationImage(),jersey1.getSalePrice(),customer);
        }else if (field.equals("proofOfAuthenticationImage")){
            listService.modifyById(this.id,jersey1.getDescription(),jersey1.getBrand(),jersey1.getSport(),jersey1.getColor(),jersey1.getJerseyImage(),jersey1.getSalePrice(),info);
            this.jersey2 = new Jersey(jersey1.getRequestState(),jersey1.getDescription(),jersey1.getBrand(),jersey1.getSport(),jersey1.getColor(),jersey1.getJerseyImage(),info,jersey1.getSalePrice(),customer);
        }else if (field.equals("salePrice")){
            listService.modifyById(this.id,jersey1.getDescription(),jersey1.getBrand(),jersey1.getSport(),jersey1.getColor(),jersey1.getJerseyImage(),Double.parseDouble(info),jersey1.getProofOfAuthenticationImage());
            this.jersey2 = new Jersey(jersey1.getRequestState(),jersey1.getDescription(),jersey1.getBrand(),jersey1.getSport(),jersey1.getColor(),jersey1.getJerseyImage(),jersey1.getProofOfAuthenticationImage(),Double.parseDouble(info),customer);
        }
    }


    @Then("the listing is updated to reflect the change")
    public void the_old_description_is_replaced_by_the_new_description(){
        assertEquals(jersey2.getRequestState(), jerseyRepo.findJerseyById(this.id).getRequestState());
        assertEquals(jersey2.getDescription(), jerseyRepo.findJerseyById(this.id).getDescription());
        assertEquals(jersey2.getBrand(), jerseyRepo.findJerseyById(this.id).getBrand());
        assertEquals(jersey2.getSport(), jerseyRepo.findJerseyById(this.id).getSport());
        assertEquals(jersey2.getColor(), jerseyRepo.findJerseyById(this.id).getColor());
        assertEquals(jersey2.getJerseyImage(), jerseyRepo.findJerseyById(this.id).getJerseyImage());
        assertEquals(jersey2.getProofOfAuthenticationImage(), jerseyRepo.findJerseyById(this.id).getProofOfAuthenticationImage());
        assertEquals(jersey2.getSalePrice(), jerseyRepo.findJerseyById(this.id).getSalePrice(),0);
    }


    
    @When("I request to delete the jersey I've selected")
    public void i_request_to_delete_the_jersey_ive_selected() throws Exception{
        listService.deleteJerseyById(this.id);
    }

    @Then("the jersey is deleted")
    public void the_jersey_is_deleted(){
        assertEquals(Optional.empty(),jerseyRepo.findById(this.id));
    }


    @Given("the jersey has been bought")
    public void i_try_to_delete_a_jersey_that_has_been_bought(){
        jersey1 = jerseyRepo.findJerseyById(id);
        jersey1.setRequestState(RequestState.Bought);
        jerseyRepo.save(jersey1);
    }

    @When("I try to delete it")
    public void i_try_to_delete_it(){
        try {
            listService.deleteJerseyById(this.id);
          } catch (Exception e) {
            this.prev_exception = e;
          }
    }

    @When("I try to modify its description to {string}")
    public void i_try_to_modify_its_description_to(String string){
        try {
            listService.modifyById(this.id, string, jersey1.getBrand(),jersey1.getSport(),jersey1.getColor(),jersey1.getJerseyImage(),jersey1.getSalePrice(),jersey1.getProofOfAuthenticationImage());
          } catch (Exception e) {
            this.prev_exception = e;
          }

    }

    @Then("the system warns me that I cannot")
    public void the_system_warns_me_that_i_cannot(){
        assertNotNull(this.prev_exception);
    }

    @And("no changes are made")
    public void no_changes_are_made(){
        assertNotNull(jerseyRepo.findById(this.id));
        assertEquals("This is a jersey.",jerseyRepo.findJerseyById(this.id).getDescription());
    }



}
