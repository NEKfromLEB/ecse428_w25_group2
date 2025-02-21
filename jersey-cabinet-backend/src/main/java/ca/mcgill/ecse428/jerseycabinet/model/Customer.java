package ca.mcgill.ecse428.jerseycabinet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class Customer extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Customer Attributes
  private String shippingAddress;

  //Customer Associations
  @OneToOne
  private Wishlist wishlist;
  @OneToOne
  private Cart ToBuy;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public Customer() {}

  public Customer(String aEmail, String aPassword, String aShippingAddress, Wishlist aWishlist, Cart aToBuy)
  {
    super(aEmail, aPassword);
    shippingAddress = aShippingAddress;
    if (aWishlist == null || aWishlist.getCustomer() != null)
    {
      throw new RuntimeException("Unable to create Customer due to aWishlist. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    wishlist = aWishlist;
    if (aToBuy == null || aToBuy.getBuyer() != null)
    {
      throw new RuntimeException("Unable to create Customer due to aToBuy. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    ToBuy = aToBuy;
  }

  public Customer(int aId, String aEmail, String aPassword, String aShippingAddress, String aKeywordsForWishlist, int aTotalPriceForToBuy)
  {
    super(aEmail, aPassword);
    shippingAddress = aShippingAddress;
    wishlist = new Wishlist(aKeywordsForWishlist, this);
    ToBuy = new Cart(aTotalPriceForToBuy, this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setShippingAddress(String aShippingAddress)
  {
    boolean wasSet = false;
    shippingAddress = aShippingAddress;
    wasSet = true;
    return wasSet;
  }

  public String getShippingAddress()
  {
    return shippingAddress;
  }
  /* Code from template association_GetOne */
  public Wishlist getWishlist()
  {
    return wishlist;
  }
  /* Code from template association_GetOne */
  public Cart getToBuy()
  {
    return ToBuy;
  }

  public void delete()
  {
    Wishlist existingWishlist = wishlist;
    wishlist = null;
    if (existingWishlist != null)
    {
      existingWishlist.delete();
    }
    Cart existingToBuy = ToBuy;
    ToBuy = null;
    if (existingToBuy != null)
    {
      existingToBuy.delete();
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "shippingAddress" + ":" + getShippingAddress()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "wishlist = "+(getWishlist()!=null?Integer.toHexString(System.identityHashCode(getWishlist())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "ToBuy = "+(getToBuy()!=null?Integer.toHexString(System.identityHashCode(getToBuy())):"null");
  }
}