package ca.mcgill.ecse428.jerseycabinet.dao;

import ca.mcgill.ecse428.jerseycabinet.model.Wishlist;
import org.springframework.data.repository.CrudRepository;
public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {
    public Wishlist findWishlistById(int id);
}
