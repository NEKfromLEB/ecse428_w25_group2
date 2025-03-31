package ca.mcgill.ecse428.jerseycabinet.service;

import org.springframework.stereotype.Service;

import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import jakarta.transaction.Transactional;

@Service
public class ModifyPaymentService {
    @Transactional
    public Jersey modifyAddressById(int id, String desc) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            throw new Exception("There is no jersey with ID" + id);
        }
        if(j.getRequestState().equals(RequestState.Listed)){
            j.setDescription(desc);
        }else{
            throw new Exception("You cannot modify a jersey that is not listed");
        }
        return jerseyRepo.save(j);
    }

    @Transactional
    public Jersey modifyHolderById(int id, String desc) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            throw new Exception("There is no jersey with ID" + id);
        }
        if(j.getRequestState().equals(RequestState.Listed)){
            j.setDescription(desc);
        }else{
            throw new Exception("You cannot modify a jersey that is not listed");
        }
        return jerseyRepo.save(j);
    }

    @Transactional
    public Jersey modifyNumberById(int id, String desc) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            throw new Exception("There is no jersey with ID" + id);
        }
        if(j.getRequestState().equals(RequestState.Listed)){
            j.setDescription(desc);
        }else{
            throw new Exception("You cannot modify a jersey that is not listed");
        }
        return jerseyRepo.save(j);
    }

    @Transactional
    public Jersey modifyCVVById(int id, String desc) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            throw new Exception("There is no jersey with ID" + id);
        }
        if(j.getRequestState().equals(RequestState.Listed)){
            j.setDescription(desc);
        }else{
            throw new Exception("You cannot modify a jersey that is not listed");
        }
        return jerseyRepo.save(j);
    }

    @Transactional
    public Jersey modifyExpiryById(int id, String desc) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            throw new Exception("There is no jersey with ID" + id);
        }
        if(j.getRequestState().equals(RequestState.Listed)){
            j.setDescription(desc);
        }else{
            throw new Exception("You cannot modify a jersey that is not listed");
        }
        return jerseyRepo.save(j);
    }
}
