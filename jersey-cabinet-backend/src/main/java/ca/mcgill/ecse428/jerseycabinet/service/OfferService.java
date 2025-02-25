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
        // 1) Check for negative price
        if (price < 0) {
            throw new IllegalArgumentException("Please enter a non-negative offer amount.");
        }

        // 2) Find the Jersey
        Jersey jersey = jerseyRepository.findById(jerseyId)
                .orElseThrow(() -> new IllegalArgumentException("Jersey not found"));

        // 3) Find the Employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        // 4) Create the Offer
        Offer.OfferKey key = new Offer.OfferKey(jersey, employee);
        Offer offer = new Offer(key, Offer.OfferState.Pending, price);

        // 5) Save and return
        return offerRepository.save(offer);
    }
}

