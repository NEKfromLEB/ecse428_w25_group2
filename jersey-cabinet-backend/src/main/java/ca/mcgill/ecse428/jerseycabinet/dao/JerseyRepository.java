package ca.mcgill.ecse428.jerseycabinet.dao;

import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import org.springframework.data.repository.CrudRepository;

public interface JerseyRepository extends CrudRepository<Jersey, Integer> {
    public Jersey findJerseyById(int id);
}
