package ca.mcgill.ecse428.jerseycabinet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class CartItem {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  @EmbeddedId
  private CartItemKey key;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CartItem() {  
  }

  public CartItem(CartItemKey key) {
    this.key = key;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public CartItemKey getKey() {
    return key;
  }

  public void setKey(CartItemKey key) {
    this.key = key;
  }

  @Override
  public String toString() {
    return "CartItem [" + "key=" + key + "]";
  }

  //------------------------
  // EMBEDDED KEY CLASS
  //------------------------

  @Embeddable
  public static class CartItemKey implements Serializable {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Jersey jersey;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public CartItemKey() {
      // Default constructor needed by JPA
    }

    public CartItemKey(Cart cart, Jersey jersey) {
      this.cart = cart;
      this.jersey = jersey;
    }

    //------------------------
    // GETTERS/SETTERS
    //------------------------

    public Cart getCart() {
      return cart;
    }

    public void setCart(Cart cart) {
      this.cart = cart;
    }

    public Jersey getJersey() {
      return jersey;
    }

    public void setJersey(Jersey jersey) {
      this.jersey = jersey;
    }

    //------------------------
    // EQUALS AND HASHCODE
    //------------------------

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof CartItemKey)) {
        return false;
      }
      CartItemKey other = (CartItemKey) obj;
      return Objects.equals(this.cart, other.cart) && Objects.equals(this.jersey, other.jersey);
    }

    @Override
    public int hashCode() {
      return Objects.hash(cart, jersey);
    }

    @Override
    public String toString() {
      return "CartItemKey [cart=" + cart + ", jersey=" + jersey + "]";
    }
  }
}
