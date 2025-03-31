package ca.mcgill.ecse428.jerseycabinet.acceptance.US019;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.PaymentMethodRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.PaymentMethod;
import ca.mcgill.ecse428.jerseycabinet.service.ModifyPaymentService;
import ca.mcgill.ecse428.jerseycabinet.service.PaymentService;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import java.sql.Date;

@CucumberContextConfiguration
@SpringBootTest
public class ModifyCardServiceSteps {
    @Autowired
    private PaymentMethodRepository paymentRepo;
    @Autowired 
    private ModifyPaymentService paymentService;
    @Autowired
    private CustomerRepository customerRepo;

    private Customer customer;
    private PaymentMethod paymentMethod1;
    private PaymentMethod paymentMethod2;
    private int id;
    private Exception prev_exception;

     @After
    public void cleanup(){
        paymentRepo.delete(this.paymentMethod1);
        paymentRepo.delete(this.paymentMethod2);
        customerRepo.delete(this.customer);
        this.prev_exception = null;
    }
    
    @Given("I am logged in as a Customer")
    public void I_am_logged_in_as_a_Customer(){
        customer = new Customer("tee@gmail.com","hehe123","123 Main Street");
        customerRepo.save(this.customer);
    }

    @And("the following card is registered to me")
    public void the_following_card_is_registered_to_me(io.cucumber.datatable.DataTable dataTable){
        Date date = Date.valueOf("03/27");
        this.paymentMethod1 = new PaymentMethod("100 Main Street", "Namir Habib", "000000000000001", "122", date, this.customer);
        paymentRepo.save(this.paymentMethod1);
        this.id = this.paymentMethod1.getId();
    }

    @When("I modify a {string} with {string}")
    public void i_modify_a_with(String field, String info) throws Exception{
        if (field.equals("billingAddress")){
            paymentService.modifyAddressById(this.id, info);
            this.paymentMethod2 = new PaymentMethod(info, paymentMethod1.getCardName(), paymentMethod1.getCardNumber(), paymentMethod1.getCardCVV(), paymentMethod1.getCardExpiryDate(), paymentMethod1.getCustomer());
        }else if (field.equals("cardHolderName")){
            paymentService.modifyHolderById(this.id, info);
            this.paymentMethod2 = new PaymentMethod(paymentMethod1.getBillingAddress(), info, paymentMethod1.getCardNumber(), paymentMethod1.getCardCVV(), paymentMethod1.getCardExpiryDate(), paymentMethod1.getCustomer());
        }else if(field.equals("cardNumber")){
            paymentService.modifyNumberById(this.id, info);
            this.paymentMethod2 = new PaymentMethod(paymentMethod1.getBillingAddress(), paymentMethod1.getCardName(), info, paymentMethod1.getCardCVV(), paymentMethod1.getCardExpiryDate(), paymentMethod1.getCustomer());
        }else if (field.equals("cardCVV")){
            paymentService.modifyCVVById(this.id, info);
            this.paymentMethod2 = new PaymentMethod(paymentMethod1.getBillingAddress(), paymentMethod1.getCardName(), paymentMethod1.getCardNumber(), info, paymentMethod1.getCardExpiryDate(), paymentMethod1.getCustomer());
        }else if (field.equals("cardExpiryDate")){
            paymentService.modifyExpiryById(this.id, info);
            Date date = Date.valueOf(info);
            this.paymentMethod2 = new PaymentMethod(paymentMethod1.getBillingAddress(), paymentMethod1.getCardName(), paymentMethod1.getCardNumber(), paymentMethod1.getCardCVV(), date, paymentMethod1.getCustomer());
        }
    }


    @Then("the card information is updated to reflect the change.")
    public void the_card_information_is_updated_to_reflect_the_change(){
        assertEquals(paymentMethod2.getRequestState(), jerseyRepo.findJerseyById(this.id).getRequestState());
        assertEquals(jersey2.getDescription(), jerseyRepo.findJerseyById(this.id).getDescription());
        assertEquals(jersey2.getBrand(), jerseyRepo.findJerseyById(this.id).getBrand());
        assertEquals(jersey2.getSport(), jerseyRepo.findJerseyById(this.id).getSport());
        assertEquals(jersey2.getColor(), jerseyRepo.findJerseyById(this.id).getColor());
        assertEquals(jersey2.getJerseyImage(), jerseyRepo.findJerseyById(this.id).getJerseyImage());
    }


    
    @When("I request to delete the card registered to my file")
    public void i_request_to_delete_the_card_registered_to_my_file() throws Exception{
        paymentService.deleteCardById(this.id);
    }

    @Then("the card is deleted")
    public void the_card_is_deleted(){
        assertEquals(Optional.empty(),paymentRepo.findById(this.id));
    }


    @When("I leave a {string} blank")
    public void i_leave_blank(){
        try {
            paymentService.deleteJerseyById(this.id);
          } catch (Exception e) {
            this.prev_exception = e;
          }
    }


    @When("I enter an {string}")
    public void i_enter_an(String string){
        try {
            paymentService.modifyDescriptionById(this.id, string);
          } catch (Exception e) {
            this.prev_exception = e;
          }

    }

    @Then("the system warns me that the card information is invalid")
    public void the_system_warns_me_that_the_card_informatino_is_invalid(){
        assertNotNull(this.prev_exception);
    }

    @And ("the form is not updated")
    public void form_is_not_updated(){

    }

}
