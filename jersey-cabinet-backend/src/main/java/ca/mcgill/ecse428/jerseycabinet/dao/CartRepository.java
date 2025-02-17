package ca.mcgill.ecse428.jerseycabinet.dao;

import ca.mcgill.ecse428ecse428.jerseycabinet.model.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Integer> {
    public Cart findCartById(int id);
}
