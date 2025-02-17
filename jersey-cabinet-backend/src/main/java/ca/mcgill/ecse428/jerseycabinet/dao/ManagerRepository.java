package ca.mcgill.ecse428.jerseycabinet.dao;

import ca.mcgill.ecse428ecse428.jerseycabinet.model.Manager;
import org.springframework.data.repository.CrudRepository;

public interface ManagerRepository extends CrudRepository<Manager, Integer> {
    public Manager findManagerById(int id);
}
