package ca.mcgill.ecse428.jerseycabinet.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;

import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import jakarta.transaction.Transactional;

@Service
public class ModifyListingService {
    @Autowired
    private JerseyRepository jerseyRepo;


    @Transactional
    public Jersey modifyDescriptionById(int id, String desc) throws Exception{
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
    public Jersey modifyBrandById(int id, String brand) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            throw new Exception("There is no jersey with ID" + id);
        }
        if(j.getRequestState().equals(RequestState.Listed)){
            j.setBrand(brand);
        }else{
            throw new Exception("You cannot modify a jersey that is not listed");
        }
        return j;
    }

    @Transactional
    public Jersey modifySportById(int id, String sport) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            throw new Exception("There is no jersey with ID" + id);
        }
        if(j.getRequestState().equals(RequestState.Listed)){
            j.setSport(sport);
        }else{
            throw new Exception("You cannot modify a jersey that is not listed");
        }
        
        return j;

    }

    @Transactional
    public Jersey modifyColourById(int id, String colour) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            throw new Exception("There is no jersey with ID" + id);
        }
        if(j.getRequestState().equals(RequestState.Listed)){
            j.setColor(colour);
        }else{
            throw new Exception("You cannot modify a jersey that is not listed");
        }
        
        return j;

    }

    @Transactional
    public Jersey modifyImageById(int id, String image) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            throw new Exception("There is no jersey with ID" + id);
        }
        if(j.getRequestState().equals(RequestState.Listed)){
            j.setJerseyImage(image);
        }else{
            throw new Exception("You cannot modify a jersey that is not listed");
        }
        
        return j;
    }

    @Transactional 
    public Jersey modifyPriceById(int id, double price) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            throw new Exception("There is no jersey with ID" + id);
        }
        
        if(j.getRequestState().equals(RequestState.Listed)){
            j.setSalePrice(price);        
        }else{
            throw new Exception("You cannot modify a jersey that is not listed");
        }
       
        return j;
    }

    @Transactional 
    public Jersey modifyAuthenById(int id, String authen) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            throw new Exception("There is no jersey with ID" + id);
        }
        if(j.getRequestState().equals(RequestState.Listed)){
            j.setProofOfAuthenticationImage(authen);
        }else{
            throw new Exception("You cannot modify a jersey that is not listed");
        }
       
        return j;
    }

    @Transactional
    public void deleteJerseyById(int id) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j==null){
            throw new Exception("There is no jersey with ID" + id);
        }
        if(j.getRequestState().equals(RequestState.Listed)){
            jerseyRepo.delete(j);
        }else{
            throw new Exception("You cannot delete a jersey that is not listed");
        }
       
        
        
    }
}
