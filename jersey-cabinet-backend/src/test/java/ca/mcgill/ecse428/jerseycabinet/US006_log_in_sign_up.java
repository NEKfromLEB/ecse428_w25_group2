package ca.mcgill.ecse428.jerseycabinet;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import ca.mcgill.ecse428.jerseycabinet.service.EmployeeLoginSignUpService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class US006_log_in_sign_up {

    @Autowired
    private EmployeeLoginSignUpService employeeService;

    private ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository employeeRepository;

    private Employee employee;
    private String errorMessage;
    private boolean loginSuccess;

    @Given("the user is on the registration page")
    public void theUserIsOnTheRegistrationPage() {
        // Simulate user being on the registration page
    }

    @When("the user enters a valid {string} and {string}")
    public void theUserEntersAValidEmailAndPassword(String email, String password) {
        try {
            employee = new Employee(email, password);
            Mockito.when(employeeRepository.findEmployeeByEmail(email)).thenReturn(null);
            Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);
            employee = employeeService.registerEmployee(email, password);
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the account should be created successfully")
    public void theAccountShouldBeCreatedSuccessfully() {
        Assertions.assertNotNull(employee);
    }

    @And("the user should see a confirmation message")
    public void theUserShouldSeeAConfirmationMessage() {
        Assertions.assertNotNull(employee, "Account creation confirmation should be shown");
    }

    @When("the user enters an invalid {string} or {string}")
    public void theUserEntersAnInvalidEmailOrPassword(String email, String password) {
        try {
            employeeService.registerEmployee(email, password);
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the system should display an appropriate error message")
    public void theSystemShouldDisplayAnAppropriateErrorMessage() {
        Assertions.assertNotNull(errorMessage);
    }

    @Given("the user has an existing account")
    public void theUserHasAnExistingAccount() {
        employee = new Employee("user@example.com", "Passw0rd!");
        Mockito.when(employeeRepository.findEmployeeByEmail("user@example.com")).thenReturn(employee);
    }

    @Given("is on the login page")
    public void isOnTheLoginPage() {
        // Simulate user navigating to the login page
    }

    @When("the user enters an incorrect {string} or {string}")
    public void theUserEntersAnIncorrectEmailOrPassword(String email, String password) {
        try {
            employeeService.loginEmployee(email, password);
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the system should display an {string} message")
    public void theSystemShouldDisplayAnInvalidCredentialsMessage(String expectedMessage) {
        Assertions.assertEquals(expectedMessage, errorMessage);
    }

    @When("the user enters an unregistered email {string}")
    public void theUserEntersAnUnregisteredEmail(String email) {
        try {
            employeeService.loginEmployee(email, "somepassword");
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the system should display a {string} message")
    public void theSystemShouldDisplayAUserNotFoundMessage(String expectedMessage) {
        Assertions.assertEquals(expectedMessage, errorMessage);
    }

    @When("the user clicks the {string} button")
    public void theUserClicksTheButton(String buttonName) {
        if (buttonName.equals("Login")) {
            try {
                employee = employeeService.loginEmployee(employee.getEmail(), employee.getPassword());
                loginSuccess = true;
            } catch (IllegalArgumentException e) {
                loginSuccess = false;
                errorMessage = e.getMessage();
            }
        } else if (buttonName.equals("Logout")) {
            employee = null;
        }
    }

    @Then("the user should be redirected to the homepage")
    public void theUserShouldBeRedirectedToTheHomepage() {
        Assertions.assertTrue(loginSuccess, "User should be logged in successfully");
    }


    @Then("the user should be redirected to the login page")
    public void theUserShouldBeRedirectedToTheLoginPage() {
        Assertions.assertNull(employee, "User should be logged out and redirected to login page");
    }

    @And("should no longer have access to protected pages")
    public void shouldNoLongerHaveAccessToProtectedPages() {
        Assertions.assertNull(employee, "Logged-out users should not access protected pages");
    }
}