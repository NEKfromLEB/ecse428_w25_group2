package ca.mcgill.ecse428.jerseycabinet.service;

import ca.mcgill.ecse428.jerseycabinet.dao.CartItemRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.CartRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Cart;
import ca.mcgill.ecse428.jerseycabinet.model.CartItem;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ManageCartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public Iterable<Cart> findAllCarts() {
        return cartRepository.findAll();
    }

    @Transactional
    public Iterable<CartItem> findAllCartItems() {
        return cartItemRepository.findAll();
    }

    @Transactional
    public Cart findCartByCustomerID(int customer_id) {
        return cartRepository.findCartByBuyer_Id(customer_id);
    }

    @Transactional
    public Iterable<CartItem> findAllCartItemsByCartID(int cart_id) {
        return cartItemRepository.findCartItemsByKey_Cart_Id(cart_id);
    }

    @Transactional
    public Iterable<CartItem> findAllCartItemsByCustomerID(int customer_id) {
        int cart_id = findCartByCustomerID(customer_id).getId();
        return findAllCartItemsByCartID(cart_id);
    }

    @Transactional Iterable<Jersey> findAllJerseysByCartID(int cart_id) {
        Iterable<CartItem> cartItems = cartItemRepository.findCartItemsByKey_Cart_Id(cart_id);
        return StreamSupport.stream(cartItems.spliterator(), false)
                .map(cartItem -> cartItem.getKey().getJersey())
                .collect(Collectors.toList());
    }

    @Transactional
    public Iterable<Jersey> findAllJerseysByCustomerID(int customer_id) {
        int cart_id = findCartByCustomerID(customer_id).getId();
        return findAllJerseysByCartID(cart_id);
    }
}
