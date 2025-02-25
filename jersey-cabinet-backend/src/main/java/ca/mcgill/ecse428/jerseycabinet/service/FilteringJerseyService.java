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
public class FilteringJerseyService {
    @Autowired
    private JerseyRepository jerseyRepo;
    
    @Transactional
    public Iterable<Jersey> findAllJerseys() {
        return jerseyRepo.findAll();
    }
    
    @Transactional
    public List<Jersey> findAllListedJerseys() {
        Iterable<Jersey> jerseys = findAllJerseys();
        List<Jersey> listedJerseys = new ArrayList<>();
        for (Jersey jersey : jerseys) {
            if (jersey.getRequestState() == RequestState.Listed) {
                listedJerseys.add(jersey);
            }
        }
        return listedJerseys;
    }
    
    /**
     * Filters the listed jerseys based on the provided sport and description substring.
     * Both filters are case insensitive. If either filter is null or empty, that filter is ignored.
     *
     * @param sport the sport to filter by
     * @param descriptionQuery the substring to search for within the jersey description
     * @return a list of jerseys matching the provided filters
     */
    @Transactional
    public List<Jersey> filterJerseys(String sport, String descriptionQuery) {
        List<Jersey> listedJerseys = findAllListedJerseys();
        List<Jersey> filteredJerseys = new ArrayList<>();
        
        for (Jersey jersey : listedJerseys) {
            boolean matchesSport = (sport == null || sport.trim().isEmpty())
                || (jersey.getSport() != null && jersey.getSport().equalsIgnoreCase(sport));
            boolean matchesDescription = (descriptionQuery == null || descriptionQuery.trim().isEmpty())
                || (jersey.getDescription() != null && jersey.getDescription().toLowerCase().contains(descriptionQuery.toLowerCase()));
            
            if (matchesSport && matchesDescription) {
                filteredJerseys.add(jersey);
            }
        }
        
        return filteredJerseys;
    }
}
