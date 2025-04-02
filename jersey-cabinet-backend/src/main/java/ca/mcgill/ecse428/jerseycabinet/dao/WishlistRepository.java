package ca.mcgill.ecse428.jerseycabinet.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Wishlist;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;

public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {
    public Wishlist findWishlistById(int id);
    
    public Wishlist findWishlistByCustomer(Customer customer);

    public ArrayList<Wishlist> findAll();

    /**
     * Retrieves jerseys that match the provided keyword with relevance scoring.
     * Matches are scored based on where the keyword matches:
     * - Brand match: 3 points
     * - Sport match: 2 points
     * - Description/Color match: 1 point
     * Only returns jerseys that are currently listed for sale.
     *
     * @param keyword The keyword to match against jerseys
     * @param requestState The request state to filter jerseys by
     * @return List of matching jerseys ordered by relevance
     */
    @Query("SELECT j FROM Jersey j WHERE j.requestState = :requestState AND " +
           "(LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.sport) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.color) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY CASE " +
           "  WHEN LOWER(j.brand) = LOWER(:keyword) THEN 3 " +
           "  WHEN LOWER(j.sport) = LOWER(:keyword) THEN 2 " +
           "  ELSE 1 END DESC")
    public List<Jersey> getMatchingJerseys(@Param("keyword") String keyword, @Param("requestState") RequestState requestState);
}
