package ca.mcgill.ecse428.jerseycabinet.service;

import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class discountService {

    @Autowired
    private JerseyRepository jerseyRepository;

    public void applyCategoryWideDiscount(String category, int discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Invalid discount percentage: Discount must be between 0% and 100%");
        }

        List<Jersey> jerseys = jerseyRepository.findBySport(category);

        if (jerseys == null || jerseys.isEmpty()) {
            throw new IllegalArgumentException("No jerseys found in the specified category");
        }

        for (Jersey jersey : jerseys) {
            double originalPrice = jersey.getSalePrice();
            double discountAmount = (originalPrice * discountPercentage) / 100.0;
            double newPrice = originalPrice - discountAmount;

            if (newPrice < 0) {
                throw new IllegalArgumentException("Discount would result in a negative price.");
            }

            jersey.setSalePrice(newPrice);
            jerseyRepository.save(jersey);
        }
    }
}