package ca.mcgill.ecse428.jerseycabinet.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.WishlistRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Wishlist;
import jakarta.transaction.Transactional;

@Service
public class CustomerAddJerseyToWishlist {
    

    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Wishlist addJerseyToWishlist(int wishlistId, String keyword) {
        Wishlist wishlist = wishlistRepository.findWishlistById(wishlistId);
        if(wishlist == null) return null;

        if(wishlist.getKeywords() == null) wishlist.setKeywords(keyword + ", ");
        else wishlist.setKeywords(wishlist.getKeywords() + keyword + ", "); // append keyword to keywords string

        return wishlistRepository.save(wishlist);
    }

    // idk if we had this method
    @Transactional
    public Wishlist getWishlistByCustomerEmail(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if(customer == null) return null;
    
        ArrayList<Wishlist> all = wishlistRepository.findAll();
        for(Wishlist wishlist : all) {
            if(wishlist.getCustomer() == null || wishlist.getCustomer().getEmail() == null) continue;

            if(wishlist.getCustomer().getEmail().equals(email)) {
                return wishlist;
            }
        }

        return null;
    }
}
