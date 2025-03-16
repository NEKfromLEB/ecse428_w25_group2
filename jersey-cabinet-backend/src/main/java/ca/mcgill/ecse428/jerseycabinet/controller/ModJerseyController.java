package ca.mcgill.ecse428.jerseycabinet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse428.jerseycabinet.dto.Jersey.JerseyRequestDTO;
import ca.mcgill.ecse428.jerseycabinet.dto.Jersey.JerseyResponseDTO;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.service.ModifyListingService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/jerseys")
public class ModJerseyController {
     @Autowired
    private ModifyListingService modService;

    @PutMapping("/jerseys/{id}")
    public JerseyResponseDTO modJersey(@RequestBody JerseyRequestDTO jersey,@PathVariable int id) throws Exception{
        Jersey modded_jersey = modService.modifyById(id, jersey.getDescription(),jersey.getBrand(),jersey.getSport(),jersey.getColor(),jersey.getJerseyImage(),jersey.getSalePrice(),jersey.getProofOfAuthenticationImage());
        return(new JerseyResponseDTO(modded_jersey.getId(),modded_jersey.getRequestState(),modded_jersey.getDescription(),modded_jersey.getBrand(),modded_jersey.getSport(),modded_jersey.getColor(),modded_jersey.getJerseyImage(),modded_jersey.getProofOfAuthenticationImage(),true,modded_jersey.getSalePrice(),modded_jersey.getEmployee().getId(),modded_jersey.getSeller().getId()));
    }
    
    @PutMapping("jerseys/{id}/delete")
    public void deleteJersey(@PathVariable int id) throws Exception{
        modService.deleteJerseyById(id);
    }
    
}
