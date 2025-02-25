package ca.mcgill.ecse428.jerseycabinet.service;

import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.OfferRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private JerseyRepository jerseyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Offer placeOffer(int jerseyId, int employeeId, int price) {
        Jersey jersey = jerseyRepository.findById(jerseyId)
                .orElseThrow(() -> new IllegalArgumentException("Jersey not found"));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Offer.OfferKey key = new Offer.OfferKey(jersey, employee);

        Offer offer = new Offer(key, Offer.OfferState.Pending, price);

        return offerRepository.save(offer);
    }

    // Overloaded method using a DTO
//    public Offer placeOffer(OfferDTO dto) {
//        // Very similar logic, just pulling jerseyId, employeeId, and price from the DTO
//        // Then build and save the Offer
//    }
}

