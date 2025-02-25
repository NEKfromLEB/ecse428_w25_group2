package ca.mcgill.ecse428.jerseycabinet.service;

import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.dto.Jersey.JerseyListingDTO;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListJerseyService {

    @Autowired
    private JerseyRepository jerseyRepository;

    @Transactional
    public Jersey getJerseyById(int id) {
        return jerseyRepository.findJerseyById(id);
    }

    @Transactional
    public Jersey updateJerseyRequestState(int id, Jersey.RequestState requestState) {
        Jersey jersey = jerseyRepository.findJerseyById(id);
        if (jersey == null) {
            throw new IllegalArgumentException("Jersey not found");
        }
        if (jersey.getBrand() == null) {
            throw new IllegalArgumentException("Brand is required");
        }
        return jerseyRepository.updateJerseyRequestState(id, requestState);
    }

    @Transactional
    public Jersey updateJerseyListing(int id, JerseyListingDTO listingDetails) {
        if (listingDetails.getSalePrice() <= 0) {
            throw new IllegalArgumentException("Price is required");
        }

        Jersey jersey = jerseyRepository.findJerseyById(id);
        if (jersey == null) {
            throw new IllegalArgumentException("Jersey not found");
        }

        jersey.setSalePrice(listingDetails.getSalePrice());
        jersey.setDescription(listingDetails.getDescription());
        jersey.setRequestState(Jersey.RequestState.Listed);

        return jerseyRepository.save(jersey);
    }

    @Transactional
    public Jersey listJerseyForSale(int id) {
        return updateJerseyRequestState(id, Jersey.RequestState.Listed);
    }
}
