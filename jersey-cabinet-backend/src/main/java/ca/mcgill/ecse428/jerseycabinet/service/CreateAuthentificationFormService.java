package ca.mcgill.ecse428.jerseycabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import jakarta.transaction.Transactional;

@Service
public class CreateAuthentificationFormService {

    @Autowired
    private JerseyRepository jerseyRepository;

    @Transactional
    public Jersey createJerseyForAuthentification(String brand, String sport, String description, String color, String proof){
        if (brand == null || brand.isEmpty() || sport == null || sport.isEmpty() || description == null || description.isEmpty() || color == null || color.isEmpty()) {
                throw new IllegalArgumentException("Please complete all required fields before submitting.");
        }
        
        

        Jersey jersey = new Jersey();
        jersey.setBrand(brand);
        jersey.setSport(sport);
        jersey.setDescription(description);
        jersey.setColor(color);
        jersey.setProofOfAuthenticationImage(proof);
        jersey.setRequestState(RequestState.Unlisted);

        jerseyRepository.save(jersey);
        return jersey;
    }

    @Transactional
    public String handleMissingProof() {
        // Warn the client and notify the store owner
        return "Proof of authenticity is missing. Submitting without proof may result in a longer verification process or rejection.";
        // Notify store owner logic here (e.g., send an email or log the event)
    }
}
