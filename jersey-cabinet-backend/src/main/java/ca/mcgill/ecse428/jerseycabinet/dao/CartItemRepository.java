package ca.mcgill.ecse428.jerseycabinet.dao;

import ca.mcgill.ecse428.jerseycabinet.model.CartItem;
import org.springframework.data.repository.CrudRepository;

public interface CartItemRepository extends CrudRepository<CartItem, CartItem.CartItemKey>{
    public CartItem findCartItemByKey(CartItem.CartItemKey key);
}
