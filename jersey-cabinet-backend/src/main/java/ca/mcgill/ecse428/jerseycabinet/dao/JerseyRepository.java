package ca.mcgill.ecse428.jerseycabinet.dao;

import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;

import org.springframework.data.repository.CrudRepository;

public interface JerseyRepository extends CrudRepository<Jersey, Integer> {
    public Jersey findJerseyById(int id);

    public long countByRequestState(RequestState unlisted);

    public Jersey findByBrandAndSportAndColorAndProofOfAuthenticationImage(String brand, String sport, String color,
            String proof);
}
