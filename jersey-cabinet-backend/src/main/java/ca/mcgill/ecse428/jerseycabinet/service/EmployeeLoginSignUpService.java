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
        if (!employeeRepository.findEmployeeByEmail(email).equals(null)) {
            throw new IllegalArgumentException("Email already in use");
        }

        if(password.equals(null) || password.length() < 7) {
            throw new IllegalArgumentException("Password must be at least 7 characters long");
        }

        Employee employee = new Employee(email, password);
        return employeeRepository.save(employee);
    }

    public Employee loginEmployee(String email, String password) {
        Employee employee = employeeRepository.findEmployeeByEmail(email);
        if (employee.equals(null) || !employee.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return employee;
    }

    public Employee getEmployeeById(int id) {
        Employee employee = employeeRepository.findEmployeeById(id);

        if(employee.equals(null)) throw new IllegalArgumentException("Id Does not Exist!");

        return employee;
    }
}