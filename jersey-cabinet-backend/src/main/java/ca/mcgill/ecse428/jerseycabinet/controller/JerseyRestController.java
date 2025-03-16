package ca.mcgill.ecse428.jerseycabinet.controller;

import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.service.ListJerseyService;
import ca.mcgill.ecse428.jerseycabinet.dto.Jersey.JerseyListingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/jerseys")
public class JerseyRestController {

    @Autowired
    private ListJerseyService jerseyService;

    @GetMapping("/{id}")
    public ResponseEntity<Jersey> getJerseyById(@PathVariable int id) {
        Jersey jersey = jerseyService.getJerseyById(id);
        return jersey != null ? ResponseEntity.ok(jersey) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/request-state")
    public ResponseEntity<Jersey> updateJerseyRequestState(
            @PathVariable int id,
            @RequestParam Jersey.RequestState requestState) {
        Jersey jersey = jerseyService.updateJerseyRequestState(id, requestState);
        return jersey != null ? ResponseEntity.ok(jersey) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/list-for-sale")
    public ResponseEntity<Jersey> listJerseyForSale(@PathVariable int id) {
        Jersey jersey = jerseyService.listJerseyForSale(id);
        return jersey != null ? ResponseEntity.ok(jersey) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/list")
    public ResponseEntity<Jersey> listJersey(
            @PathVariable int id) {
        Jersey jersey = jerseyService.updateJerseyRequestState(id, Jersey.RequestState.Listed);
        return jersey != null ? ResponseEntity.ok(jersey) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/listing")
    public ResponseEntity<Jersey> updateJerseyListing(
            @PathVariable int id,
            @RequestBody JerseyListingDTO listingDetails) {
        Jersey jersey = jerseyService.getJerseyById(id);
        if (jersey == null) {
            return ResponseEntity.notFound().build();
        }
        
        jersey = jerseyService.updateJerseyRequestState(id, Jersey.RequestState.Listed);
        return ResponseEntity.ok(jersey);
    }

}
