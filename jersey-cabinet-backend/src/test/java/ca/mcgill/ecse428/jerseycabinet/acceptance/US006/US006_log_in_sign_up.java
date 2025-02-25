package ca.mcgill.ecse428.jerseycabinet.acceptance.US006;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import ca.mcgill.ecse428.jerseycabinet.service.EmployeeLoginSignUpService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
public class US006_log_in_sign_up {

    @Autowired
    private EmployeeLoginSignUpService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;
    private Employee loginEmployee;
    private Employee existingEmployee;
    private Employee nonExistingEmployee;
    private String incorrectEmail = null;
    private String errorMessage = null;
    private boolean toRegister;


    @Given("the user is on the registration page")
    public void theUserIsOnTheRegistrationPage() {
        employeeRepository.deleteAll();
        toRegister = true;
        // Simulate user being on the registration page
    }

    @When("the user enters a valid {string} and {string}")
    public void theUserEntersAValidEmailAndPassword(String email, String password) {
       
        try {
            if(toRegister) {
                // create employee
                employee = employeeService.registerEmployee(email, password);
                errorMessage = null;
            }
            else {
                loginEmployee = employeeService.loginEmployee(email, password);
                errorMessage = null;
            }
        }
        catch(Exception e) {
            errorMessage = e.getMessage();
        }
        
       
    }

    @And("clicks the Sign Up button")
    public void clicksTheSignUpButton() {
        
    }

    @Then("the account should be created successfully")
    public void theAccountShouldBeCreatedSuccessfully() {
        assertNotNull(employee);
        assertNotNull(employeeRepository.findEmployeeByEmail(employee.getEmail()), "Employee not found in DB");
    }

    @And("the user should see a confirmation message")
    public void theUserShouldSeeAConfirmationMessage() {
        // this will be done in frontend
    }

    @When("the user enters an invalid {string} or {string}")
    public void theUserEntersAnInvalidEmailOrPassword(String email, String password) {
        try {
            incorrectEmail = email;
            nonExistingEmployee = employeeService.registerEmployee(email, password);
        }
        catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the system should display an appropriate error message")
    public void theSystemShouldDisplayAnAppropriateErrorMessage() {
        assertNotNull(errorMessage);
        errorMessage = null;
        assertNotNull(incorrectEmail);
        assertNull(employeeRepository.findEmployeeByEmail(incorrectEmail));
    }

    @Given("the user has an existing account with {string} and {string}")
    public void theUserHasAnExistingAccountWithEmailAndPassword(String email, String password) {
        employeeRepository.deleteAll();

        Employee example = new Employee(email, password);
        employeeRepository.save(example);
    }

    @And("is on the login page")
    public void isOnTheLoginPage() {
        toRegister = false;
        // Simulate user navigating to the login page
    }

    @And("clicks the Login button")
    public void clicksTheLoginButton() {
        // clicks button
    }

    @Then("the user should be redirected to the homepage") 
    public void theUserShouldBeRedirectedToTheHomepage(){
        assertNull(errorMessage);
        assertNotNull(loginEmployee);
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        employeeRepository.deleteAll();
        toRegister = false;
    }

    @When("the user enters an incorrect {string} or {string}")
    public void theUserEntersAnIncorrectEmailOrPassword(String email, String password) {
        try {
            nonExistingEmployee = employeeService.loginEmployee(email, password);
        }
        catch(Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the system should display an {string} message")
    public void theSystemShouldDisplayAnInvalidCredentialsMessage(String expectedMessage) {
        assertNotNull(errorMessage);
    }

    @When("the user enters an unregistered email newuser@example.com")
    public void theUserEntersAnUnregisteredEmail() {
        try {
            nonExistingEmployee = employeeService.loginEmployee("newuser@example.com", "APassword123");
        }
        catch(Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the system should display a User not found message")
    public void theSystemShouldDisplayAUserNotFoundMessage() {
        assertNotNull(errorMessage);
        errorMessage = null;
    }

}