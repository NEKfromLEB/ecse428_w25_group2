package ca.mcgill.ecse428.jerseycabinet.dao;

import ca.mcgill.ecse428ecse428.jerseycabinet.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    public Customer findCustomerById(int id);
}
