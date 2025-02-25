package ca.mcgill.ecse428.jerseycabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;

@Service
public class EmployeeLoginSignUpService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee registerEmployee(String email, String password) {
        if (employeeRepository.findEmployeeByEmail(email) != null) {
            throw new IllegalArgumentException("Email already in use");
        }

        if(password == null || password.length() < 7) {
            throw new IllegalArgumentException("Password must be at least 7 characters long");
        }

        int i = email.indexOf("@");
        if (i == -1 || i == 0 || i == email.length() - 1) {
            throw new IllegalArgumentException("email must contain an @");
        }

        if(!(email.chars().filter(ch -> ch == '@').count() == 1)) {
            throw new IllegalArgumentException("email must contain only 1 @");
        }

        i = email.indexOf(".com");
        if (i <= 0 || i != email.length() - 4) {
            throw new IllegalArgumentException("email must contain .com at the end");
        }

        if(!email.strip().equals(email)) {
            throw new IllegalArgumentException("Email must not contain spaces");
        }

        if(email.length() < 7) {
            throw new IllegalArgumentException("Email too short");
        }

        Employee employee = new Employee(email, password);
        return employeeRepository.save(employee);
    }

    public Employee loginEmployee(String email, String password) {
        Employee employee = employeeRepository.findEmployeeByEmail(email);
        if (employee == null || !employee.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return employee;
    }

    public Employee getEmployeeById(int id) {
        Employee employee = employeeRepository.findEmployeeById(id);

        if(employee == null) throw new IllegalArgumentException("Id Does not Exist!");

        return employee;
    }
}