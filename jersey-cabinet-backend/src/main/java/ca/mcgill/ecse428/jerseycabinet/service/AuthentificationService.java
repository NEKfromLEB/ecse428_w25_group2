package ca.mcgill.ecse428.jerseycabinet.service;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import jakarta.transaction.Transactional;

@Service
public class AuthentificationService {
    @Autowired
    private JerseyRepository jerseyRepo;

    @Transactional
    public Jersey findJerseyById(int id){
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            //throw error
        }
        return j;
    }

    @Transactional
    public ArrayList<Jersey> findAllUnreviewedJerseys(){
        Iterable<Jersey> all_jerseys = jerseyRepo.findAll();
        ArrayList<Jersey> unlisted_jerseys = new ArrayList();
        for (Jersey j: all_jerseys){
            if (j.getRequestState() == null){
                unlisted_jerseys.add(j);
            }
        }
        return unlisted_jerseys;

    }

    @Transactional
    public void acceptRequestById(int id){
        Jersey j = jerseyRepo.findJerseyById(id);
        j.setRequestState(RequestState.Unlisted);

    }

    @Transactional
    public void rejectRequestById(int id){
        Jersey j = jerseyRepo.findJerseyById(id);
        j.setRequestState(RequestState.Rejected);
    }

    
}
