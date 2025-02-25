package ca.mcgill.ecse428.jerseycabinet.service;

import ca.mcgill.ecse428.jerseycabinet.dao.CartItemRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.CartRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Cart;
import ca.mcgill.ecse428.jerseycabinet.model.CartItem;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManageCartService {

  @Autowired private CartRepository cartRepository;
  @Autowired private CartItemRepository cartItemRepository;

  // Find methods

  /**
   * Find all carts in database (Helper func)
   *
   * @return all carts
   * @author Mathieu Pestel
   */
  @Transactional
  public Iterable<Cart> findAllCarts() {
    return cartRepository.findAll();
  }

  /**
   * Find all cart items in database (Helper func)
   *
   * @return all cart items
   * @author Mathieu Pestel
   */
  @Transactional
  public Iterable<CartItem> findAllCartItems() {
    return cartItemRepository.findAll();
  }

  /**
   * Find a customer's cart instance
   *
   * @param customer_id Queried customer's ID
   * @return Queried customer's cart
   * @author Mathieu Pestel
   */
  @Transactional
  public Cart findCartByCustomerID(int customer_id) {
    return cartRepository.findCartByBuyer_Id(customer_id);
  }

  /**
   * Find all cart items in a cart
   *
   * @param cart_id Queried cart's ID
   * @return Queried cart's cartItems
   * @author Mathieu Pestel
   */
  @Transactional
  public Iterable<CartItem> findAllCartItemsByCartID(int cart_id) {
    return cartItemRepository.findCartItemsByKey_Cart_Id(cart_id);
  }

  /**
   * Find all cart items belonging to a customer
   *
   * @param customer_id Queried customer's ID
   * @return Queried customer's cartItems
   * @author Mathieu Pestel
   */
  @Transactional
  public Iterable<CartItem> findAllCartItemsByCustomerID(int customer_id) {
    int cart_id = findCartByCustomerID(customer_id).getId();
    return findAllCartItemsByCartID(cart_id);
  }

  /**
   * Find all jersey instances in a cart
   *
   * @param cart_id Queried cart's ID
   * @return Queried cart's jersey instances
   * @author Mathieu Pestel
   */
  @Transactional
  Iterable<Jersey> findAllJerseysByCartID(int cart_id) {
    Iterable<CartItem> cartItems = cartItemRepository.findCartItemsByKey_Cart_Id(cart_id);
    return StreamSupport.stream(cartItems.spliterator(), false)
        .map(cartItem -> cartItem.getKey().getJersey())
        .collect(Collectors.toList());
  }

  /**
   * Find all jersey instances belonging to a customer
   *
   * @param customer_id Queried customer's ID
   * @return Queried customer's jersey instances
   * @author Mathieu Pestel
   */
  @Transactional
  public Iterable<Jersey> findAllJerseysByCustomerID(int customer_id) {
    int cart_id = findCartByCustomerID(customer_id).getId();
    return findAllJerseysByCartID(cart_id);
  }

  // Adding items to cart

  /**
   * Add a jersey to a customer's cart, ensuring no invalid jerseys are added, as well as updating cart total price.
   * @param customer_id ID of customer adding jersey to their cart
   * @param item Jersey Instance being added
   * @return CartItem association instance
   */
  @Transactional
  public CartItem addItemToCart(int customer_id, Jersey item) {

    Cart customerCart = findCartByCustomerID(customer_id);
    ArrayList<Jersey> customerJerseys = (ArrayList<Jersey>) findAllJerseysByCustomerID(customer_id);

    // out of stock/unlisted error
    if (item.getRequestState() != Jersey.RequestState.Listed) {
      throw new IllegalArgumentException("Item is not for sale");
    }

    // if item already in cart
    if (customerJerseys.stream().anyMatch(jersey -> jersey.getId() == item.getId())) {
      throw new IllegalArgumentException("Item already in cart");
    }

    // update total price of cart
    int previousPrice = customerCart.getTotalPrice();
    customerCart.setTotalPrice((int) (previousPrice + item.getSalePrice()));

    return new CartItem(new CartItem.CartItemKey(customerCart, item));
  }

}
