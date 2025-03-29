package ca.mcgill.ecse428.jerseycabinet.service;

import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class discountService {

    @Autowired
    private JerseyRepository jerseyRepository;

    public void applyCategoryWideDiscount(String sportCategory, double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Invalid discount percentage: Discount must be between 0% and 100%");
        }

        List<Jersey> jerseys = jerseyRepository.findBySport(sportCategory);
        if (jerseys.isEmpty()) {
            throw new IllegalArgumentException("No jerseys found in the specified category");
        }

        for (Jersey jersey : jerseys) {
            double newSalePrice = jersey.getSalePrice() * (1 - discountPercentage / 100);
            if (newSalePrice < 0) {
                throw new IllegalArgumentException("Invalid operation: Sale price cannot be negative");
            }
            jersey.setSalePrice(newSalePrice);
        }

        jerseyRepository.saveAll(jerseys);
    }
}