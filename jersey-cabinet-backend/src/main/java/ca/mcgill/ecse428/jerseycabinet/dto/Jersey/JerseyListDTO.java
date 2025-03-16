package ca.mcgill.ecse428.jerseycabinet.dto.Jersey;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse428.jerseycabinet.model.Jersey;

public class JerseyListDTO {
    private List<JerseyResponseDTO> jerseys = new ArrayList<JerseyResponseDTO>();
    
    
    public JerseyListDTO(List<Jersey> jerseys) {
    	for (Jersey jersey: jerseys) {
    		this.jerseys.add(new JerseyResponseDTO(jersey.getId(),jersey.getRequestState(),jersey.getDescription(),jersey.getBrand(),jersey.getSport(),jersey.getColor(),jersey.getJerseyImage(),jersey.getProofOfAuthenticationImage(),true,jersey.getSalePrice(),jersey.getEmployee().getId(),jersey.getSeller().getId()));
    	}
    }
    
    public List<JerseyResponseDTO> getRegistrations(){
    	return this.jerseys;
    }
}
