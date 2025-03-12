package ca.mcgill.ecse428.jerseycabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.OfferRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import ca.mcgill.ecse428.jerseycabinet.model.Offer;
import ca.mcgill.ecse428.jerseycabinet.model.Offer.OfferState;

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

        if (jersey.getRequestState() != RequestState.Unlisted){
            throw new IllegalArgumentException("Invalid jersey state.");
        }

        // 4) Create the Offer
        Offer.OfferKey key = new Offer.OfferKey(jersey, employee);
        Offer offer = new Offer(key, Offer.OfferState.Pending, price);

        if (offerRepository.findOfferByKey(key) != null){
            throw new IllegalArgumentException("You have already placed an offer for this jersey.");
        }

        // 5) Save and return
        return offerRepository.save(offer);
    }

    public Offer acceptOffer(int jerseyId, int employeeId){
        Jersey jersey = jerseyRepository.findById(jerseyId)
                .orElseThrow(() -> new IllegalArgumentException("Jersey not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        if (jersey.getRequestState() != RequestState.Unlisted){
            throw new IllegalArgumentException("Invalid jersey state.");
        }

        Offer.OfferKey key = new Offer.OfferKey(jersey, employee);
        Offer offer = offerRepository.findOfferByKey(key);

        offer.setOfferState(OfferState.Accepted);
        jersey.setRequestState(RequestState.Listed);

        return offerRepository.save(offer);
    }

    public Offer rejectOffer(int jerseyId, int employeeId){
        Jersey jersey = jerseyRepository.findById(jerseyId)
                .orElseThrow(() -> new IllegalArgumentException("Jersey not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        if (jersey.getRequestState() != RequestState.Unlisted){
            throw new IllegalArgumentException("Invalid jersey state.");
        }
        
        Offer.OfferKey key = new Offer.OfferKey(jersey, employee);
        Offer offer = offerRepository.findOfferByKey(key);

        offer.setOfferState(OfferState.Rejected);

        return offer;
    }

    public Offer counterOffer(int jerseyId, int employeeId, int price){
        Jersey jersey = jerseyRepository.findById(jerseyId)
                .orElseThrow(() -> new IllegalArgumentException("Jersey not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        
        Offer.OfferKey key = new Offer.OfferKey(jersey, employee);
        Offer offer = offerRepository.findOfferByKey(key);

        offer.setOfferState(OfferState.Pending);
        offer.setPrice(price);

        return offerRepository.save(offer);
    }
}

