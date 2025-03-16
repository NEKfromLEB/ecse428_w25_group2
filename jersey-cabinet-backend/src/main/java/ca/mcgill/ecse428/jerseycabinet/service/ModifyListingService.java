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
    public Jersey modifyById(int id, String desc, String brand, String sport, String colour, String image, double price, String authen) throws Exception{
        Jersey j = jerseyRepo.findJerseyById(id);
        if (j == null){
            throw new Exception("There is no jersey with ID" + id);
        }
        if(j.getRequestState().equals(RequestState.Listed)){
            j.setDescription(desc);
            j.setBrand(brand);
            j.setSport(sport);
            j.setColor(colour);
            j.setJerseyImage(image);
            j.setSalePrice(price);
            j.setProofOfAuthenticationImage(authen);
        }else{
            throw new Exception("You cannot modify a jersey that is not listed");
        }
        return jerseyRepo.save(j);
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
