package ca.mcgill.ecse428.jerseycabinet.controller;

import ca.mcgill.ecse428.jerseycabinet.dto.OfferDTO;
import ca.mcgill.ecse428.jerseycabinet.model.Offer;
import ca.mcgill.ecse428.jerseycabinet.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @PostMapping
    public ResponseEntity<OfferDTO> placeOffer(@RequestBody OfferDTO offerDTO) {
        int jerseyId = offerDTO.getJerseyId();
        int employeeId = offerDTO.getEmployeeId();
        int price = offerDTO.getPrice();

        Offer createdOffer = offerService.placeOffer(jerseyId, employeeId, price);

        OfferDTO responseDto = convertOfferToDTO(createdOffer);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    private OfferDTO convertOfferToDTO(Offer offer) {
        OfferDTO dto = new OfferDTO();
        dto.setJerseyId(offer.getKey().getJersey().getId());
        dto.setEmployeeId(offer.getKey().getEmployee().getId());
        dto.setPrice(offer.getPrice());
        dto.setOfferState(offer.getOfferState().toString());
        return dto;
    }
}
