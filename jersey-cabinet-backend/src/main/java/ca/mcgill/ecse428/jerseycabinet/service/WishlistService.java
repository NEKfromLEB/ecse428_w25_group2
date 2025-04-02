package ca.mcgill.ecse428.jerseycabinet.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
     * Results are ordered by relevance, with brand matches prioritized.
     *
     * @param keywords List of keywords to match against jerseys
     * @return List of matching jerseys, or empty list if no matches found
     * @throws IllegalArgumentException if keywords list is null or empty
     */
    @Transactional
    public List<Jersey> identifyJerseys(List<String> keywords) {
        validateKeywords(keywords);
        
        Set<Jersey> matchingJerseys = new HashSet<>();
        List<String> processedKeywords = preprocessKeywords(keywords);
        
        // Search for each keyword and add matching jerseys to the set
        for (String keyword : processedKeywords) {
            List<Jersey> jerseys = wishlistRepository.getMatchingJerseys(keyword, RequestState.Listed);
            matchingJerseys.addAll(jerseys);
        }

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
        validateNotificationParams(customer, jersey);

        var wishlist = wishlistRepository.findWishlistByCustomer(customer);
        if (wishlist == null || wishlist.getKeywords() == null || wishlist.getKeywords().isEmpty()) {
            return false;
        }

        // Check if the jersey matches any of the wishlist keywords
        List<String> keywords = preprocessKeywords(List.of(wishlist.getKeywords().split(",")));
        for (String keyword : keywords) {
            if (isJerseyMatch(jersey, keyword)) {
                String message = formatNotificationMessage(jersey);
                return notificationService.sendNotification(message);
            }
        }

        return false;
    }

    private void validateKeywords(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords list cannot be null or empty");
        }
    }

    private void validateNotificationParams(Customer customer, Jersey jersey) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (jersey == null) {
            throw new IllegalArgumentException("Jersey cannot be null");
        }
    }

    private List<String> preprocessKeywords(List<String> keywords) {
        return keywords.stream()
            .filter(k -> k != null && !k.trim().isEmpty())
            .map(String::trim)
            .map(String::toLowerCase)
            .distinct()
            .collect(Collectors.toList());
    }

    private boolean isJerseyMatch(Jersey jersey, String keyword) {
        return jersey.getDescription().toLowerCase().contains(keyword) ||
               jersey.getBrand().toLowerCase().contains(keyword) ||
               jersey.getSport().toLowerCase().contains(keyword) ||
               jersey.getColor().toLowerCase().contains(keyword);
    }

    private String formatNotificationMessage(Jersey jersey) {
        return String.format("A jersey matching your wishlist is on sale! %s - $%.2f", 
            jersey.getDescription(), jersey.getSalePrice());
    }
} 