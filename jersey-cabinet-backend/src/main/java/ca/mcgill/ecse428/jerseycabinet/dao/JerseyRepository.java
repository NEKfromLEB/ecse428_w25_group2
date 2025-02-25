package ca.mcgill.ecse428.jerseycabinet.dao;

import ca.mcgill.ecse428.jerseycabinet.model.Jersey;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
=======
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;


import org.springframework.data.repository.CrudRepository;

public interface JerseyRepository extends CrudRepository<Jersey, Integer> {
    public Jersey findJerseyById(int id);


    @Transactional
    @Modifying
    @Query("UPDATE Jersey j SET j.requestState = :requestState WHERE j.id = :id")
    public Jersey updateJerseyRequestState(int id, Jersey.RequestState requestState);

    public long countByRequestState(RequestState unlisted);

    public Jersey findByBrandAndSportAndColorAndProofOfAuthenticationImage(String brand, String sport, String color,
            String proof);
}
