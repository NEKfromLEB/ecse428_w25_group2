package ca.mcgill.ecse428.jerseycabinet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import jakarta.transaction.Transactional;

@Service
public class SearchJerseyService {
    @Autowired
    private JerseyRepository jerseyRepo;

    @Transactional
    public Iterable<Jersey> findAllJerseys(){
        return jerseyRepo.findAll();
    }

    @Transactional
    public Jersey findJerseyById(int jersey_id){
        return jerseyRepo.findJerseyById(jersey_id);
    }

    @Transactional
    public List<Jersey> findAllListedJerseys(){
        Iterable<Jersey> jerseys = findAllJerseys();
        List<Jersey> listedJerseys = new ArrayList<>();

        for (Jersey jersey : jerseys){
            if (jersey.getRequestState() == RequestState.Listed){
                listedJerseys.add(jersey);
            }
        }
        return listedJerseys;
    }
    
    @Transactional
    public List<Jersey> findAllJerseysOfSport(String sport){
        Iterable<Jersey> jerseys = findAllJerseys();
        List<Jersey> jerseysOfSport = new ArrayList<>();

        for (Jersey jersey : jerseys){
            if (jersey.getSport().equals(sport)){
                jerseysOfSport.add(jersey);
            }
        }
        return jerseysOfSport;
    }
}
