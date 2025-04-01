package ca.mcgill.ecse428.jerseycabinet.acceptance.US017;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import ca.mcgill.ecse428.jerseycabinet.service.ModifyEmployeeService;

@CucumberContextConfiguration
@SpringBootTest
public class ModifyEmployeeSteps {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModifyEmployeeService modifyEmployeeService;

    private Employee updated;
    private Employee testEmployee;
    private Exception exception;

    @Given("I am logged in as an Employee")
    public void iAmLoggedInAsAnEmployee() {
        testEmployee = new Employee("employee@email.com", "Pass123");
        updated = new Employee("newemail@gmail.com", "NewPass123");
        employeeRepository.save(testEmployee);
        testEmployee = employeeRepository.findEmployeeById(testEmployee.getId());
    }

    @When("I change my email to {string} and password to {string}")
    public void i_change_my_email_to_and_password_to(String email, String password) {  
        exception = null;
        try {
            modifyEmployeeService.modifyEmployeeAccount(testEmployee.getId(), email, password);
        } catch (Exception e) {
            exception = e;
        }
        testEmployee = employeeRepository.findEmployeeById(testEmployee.getId());
    }

    @Then("my account information is updated")
    public void myAccountInformationIsUpdated() {
        assertEquals("newemail@gmail.com", testEmployee.getEmail());
        assertEquals("NewPass123", testEmployee.getPassword());
    }

    // Scenario 2: Successfully Modify One Field at a Time (Alternative Flow)
    @Given("I attempt to modify my email to {string} and leave the password unchanged")
    public void iAttemptToModifyMyEmailAndLeavePasswordUnchanged(String newEmail) {
        testEmployee.setEmail(newEmail);
        exception = null;
        try {
            modifyEmployeeService.modifyEmployeeAccount(testEmployee.getId(), newEmail, null);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("I submit the changes to email")
    public void iSubmitTheChangesToEmail() {
        exception = null;
        try {
            modifyEmployeeService.modifyEmployeeAccount(testEmployee.getId(), testEmployee.getEmail(), testEmployee.getPassword());
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("my account information is updated with the new email")
    public void myAccountInformationIsUpdatedWithTheNewEmail() {
        Employee updatedEmployee = employeeRepository.findEmployeeById(testEmployee.getId());
        assertNotNull(updatedEmployee);
        assertEquals("newemail@yahoo.com", updatedEmployee.getEmail());
        assertEquals(testEmployee.getPassword(), updatedEmployee.getPassword());
    }

    @Then("no changes are made to the password")
    public void noChangesAreMadeToThePassword() {
        Employee updatedEmployee = employeeRepository.findEmployeeById(testEmployee.getId());
        assertNotNull(updatedEmployee);
        assertEquals(testEmployee.getPassword(), updatedEmployee.getPassword());
    }

    // Scenario 3: Successfully Modify Only One Field at a Time (Alternative Flow)
    @Given("I attempt to modify my password to {string} and leave the email unchanged")
    public void iAttemptToModifyMyPasswordAndLeaveEmailUnchanged(String newPassword) {
        testEmployee.setPassword(newPassword);
        exception = null;
        try {
            modifyEmployeeService.modifyEmployeeAccount(testEmployee.getId(), null, newPassword);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("I submit the changes to password")
    public void iSubmitTheChangesToPassword() {
        exception = null;
        try {
            modifyEmployeeService.modifyEmployeeAccount(testEmployee.getId(), testEmployee.getEmail(), testEmployee.getPassword());
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("my account information is updated with the new password")
    public void myAccountInformationIsUpdatedWithTheNewPassword() {
        Employee updatedEmployee = employeeRepository.findEmployeeById(testEmployee.getId());
        assertNotNull(updatedEmployee);
        assertEquals(testEmployee.getEmail(), updatedEmployee.getEmail());
        assertEquals("NewPass514", updatedEmployee.getPassword());
    }

    @Then("no changes are made to the email")
    public void noChangesAreMadeToTheEmail() {
        Employee updatedEmployee = employeeRepository.findEmployeeById(testEmployee.getId());
        assertNotNull(updatedEmployee);
        assertEquals(testEmployee.getEmail(), updatedEmployee.getEmail());
    }

    // Scenario 4: Attempting to Modify a Non-Existent Account (Error Flow)
    @Given("the employee account does not exist")
    public void theEmployeeAccountDoesNotExist() {
        testEmployee = null; // Simulating non-existent account
    }

    @When("I try to modify the email to {string}")
    public void iTryToModifyTheEmailTo(String newEmail) {
        exception = null;
        try {
            modifyEmployeeService.modifyEmployeeAccount(0, newEmail, null); // Test non-existent account
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the system displays an error message: {string}")
    public void theSystemDisplaysAnErrorMessage(String errorMessage) {
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Then("no changes are made")
    public void noChangesAreMade() {
        if (testEmployee != null) {
            Employee updatedEmployee = employeeRepository.findEmployeeById(testEmployee.getId());
            assertEquals(testEmployee.getEmail(), updatedEmployee.getEmail());
            assertEquals(testEmployee.getPassword(), updatedEmployee.getPassword());
        }
    }
}
