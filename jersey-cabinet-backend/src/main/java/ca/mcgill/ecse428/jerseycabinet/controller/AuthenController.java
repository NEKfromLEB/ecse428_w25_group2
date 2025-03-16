package ca.mcgill.ecse428.jerseycabinet.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.service.AuthenticationService;
import ca.mcgill.ecse428.jerseycabinet.dto.Jersey.JerseyListDTO;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/jerseys")
public class AuthenController {
    
    @Autowired
    private AuthenticationService authenService;

    @GetMapping("/jerseys/unreviewed")
    public JerseyListDTO getUnreviewed(){
        ArrayList<Jersey> jerseys= authenService.findAllUnreviewedJerseys();
        return(new JerseyListDTO(jerseys));
    }

    @PutMapping("{employee}/jerseys/{id}/accept")
    public void acceptAuthenReq(@PathVariable(name="id") int id, @PathVariable(name="employee") Employee employee){
        authenService.acceptRequestById(id, employee);
    }  

    @PutMapping("{employee}/jerseys/{id}/reject")
    public void rejectAuthenReq(@PathVariable(name="id") int id, @PathVariable(name="employee") Employee employee){
       authenService.rejectRequestById(id, employee);
    }
    
}
