package ca.mcgill.ecse428.jerseycabinet.service;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {
    @Autowired
    private JerseyRepository jerseyRepo;

    @Transactional
    public Jersey findJerseyById(int id) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            throw new Exception("There is no jersey with ID" + id);
        }
        return j;
    }

    @Transactional
    public ArrayList<Jersey> findAllUnreviewedJerseys(){
        Iterable<Jersey> all_jerseys = jerseyRepo.findAll();
        ArrayList<Jersey> unlisted_jerseys = new ArrayList<>();
        for (Jersey j: all_jerseys){
            if (j.getRequestState() == null && j.getEmployee() ==null){
                unlisted_jerseys.add(j);
            }
        }
        return unlisted_jerseys;

    }

    @Transactional
    public Jersey acceptRequestById(int id,Employee employee){
        Jersey j = jerseyRepo.findJerseyById(id);
        j.setRequestState(RequestState.Unlisted);
        j.setEmployee(employee);
        return jerseyRepo.save(j);
    }

    @Transactional
    public Jersey rejectRequestById(int id, Employee employee){
        Jersey j = jerseyRepo.findJerseyById(id);
        j.setRequestState(RequestState.Rejected);
        j.setEmployee(employee);
        return jerseyRepo.save(j);
    }
    
}
