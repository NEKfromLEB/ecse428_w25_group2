package ca.mcgill.ecse428.jerseycabinet.dao;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse428.jerseycabinet.model.Wishlist;
public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {
    public Wishlist findWishlistById(int id);

    public ArrayList<Wishlist> findAll();
}
