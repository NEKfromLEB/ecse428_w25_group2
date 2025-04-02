package ca.mcgill.ecse428.jerseycabinet.service;
import org.springframework.beans.factory.annotation.Autowired;
import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ModifyEmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public Employee modifyEmployeeAccount(int employeeId, String newEmail, String newPassword) {
        Employee employee = employeeRepository.findEmployeeById(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Account not found.");
        }

        if (newEmail != null && !newEmail.isEmpty()) {
            employee.setEmail(newEmail);
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            employee.setPassword(newPassword);
        }
        
        return employeeRepository.save(employee);
    }
}
