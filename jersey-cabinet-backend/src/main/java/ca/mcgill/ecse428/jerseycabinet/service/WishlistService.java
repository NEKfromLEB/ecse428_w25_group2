package ca.mcgill.ecse428.jerseycabinet.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse428.jerseycabinet.dao.WishlistRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;

@Service
public class WishlistService {
    
    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * Identifies jerseys that match any of the provided wishlist keywords.
     * This method searches through available jerseys and returns those that match
     * the provided keywords in their description, brand, sport, or color.
     *
     * @param keywords List of keywords to match against jerseys
     * @return List of matching jerseys, or empty list if no matches found
     * @throws IllegalArgumentException if keywords list is null or empty
     */
    @Transactional
    public List<Jersey> identifyJerseys(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords list cannot be null or empty");
        }

        Set<Jersey> matchingJerseys = new HashSet<>();
        
        // Search for each keyword and add matching jerseys to the set
        for (String keyword : keywords) {
            if (keyword != null && !keyword.trim().isEmpty()) {
                List<Jersey> jerseys = wishlistRepository.getMatchingJerseys(keyword.trim(), RequestState.Listed);
                matchingJerseys.addAll(jerseys);
            }
        }

        // Convert set to list to return
        return new ArrayList<>(matchingJerseys);
    }

    /**
     * Notifies a customer when a jersey in their wishlist goes on sale.
     * This method checks if the jersey matches any of the customer's wishlist keywords
     * and sends a notification if it does.
     *
     * @param customer The customer to notify
     * @param jersey The jersey that went on sale
     * @return true if notification was sent successfully, false otherwise
     * @throws IllegalArgumentException if customer or jersey is null
     */
    @Transactional
    public boolean notifyCustomerOfSale(Customer customer, Jersey jersey) {
        if (customer == null || jersey == null) {
            throw new IllegalArgumentException("Customer and jersey information are required.");
        }

        var wishlist = wishlistRepository.findWishlistByCustomer(customer);
        if (wishlist == null || wishlist.getKeywords() == null || wishlist.getKeywords().isEmpty()) {
            return false;
        }

        // Check if the jersey matches any of the wishlist keywords
        String[] keywords = wishlist.getKeywords().split(",");
        for (String keyword : keywords) {
            keyword = keyword.trim().toLowerCase();
            if (keyword.isEmpty()) continue;

            // Check if keyword matches any jersey attributes
            if (jersey.getDescription().toLowerCase().contains(keyword) ||
                jersey.getBrand().toLowerCase().contains(keyword) ||
                jersey.getSport().toLowerCase().contains(keyword) ||
                jersey.getColor().toLowerCase().contains(keyword)) {
                
                String message = String.format("A jersey matching your wishlist is on sale! %s - $%.2f", 
                    jersey.getDescription(), jersey.getSalePrice());
                return notificationService.sendNotification(message);
            }
        }

        return false;
    }
} 