package ca.mcgill.ecse428.jerseycabinet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Cart
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Cart Attributes
  @Id
  @GeneratedValue
  private int id;
  private int totalPrice;

  //Cart Associations
  @OneToOne
  private Customer buyer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Cart(){}

  public Cart(int aTotalPrice, Customer aBuyer)
  {
    totalPrice = aTotalPrice;
    if (aBuyer == null || aBuyer.getToBuy() != null)
    {
      throw new RuntimeException("Unable to create Cart due to aBuyer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    buyer = aBuyer;
  }

  public Cart(int aId, int aTotalPrice, String aEmailForBuyer, String aPasswordForBuyer, String aShippingAddressForBuyer, Wishlist aWishlistForBuyer)
  {
    totalPrice = aTotalPrice;
    buyer = new Customer(aEmailForBuyer, aPasswordForBuyer, aShippingAddressForBuyer, aWishlistForBuyer, this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setTotalPrice(int aTotalPrice)
  {
    boolean wasSet = false;
    totalPrice = aTotalPrice;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }

  public int getTotalPrice()
  {
    return totalPrice;
  }
  /* Code from template association_GetOne */
  public Customer getBuyer()
  {
    return buyer;
  }

  public void delete()
  {
    Customer existingBuyer = buyer;
    buyer = null;
    if (existingBuyer != null)
    {
      existingBuyer.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "totalPrice" + ":" + getTotalPrice()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "buyer = "+(getBuyer()!=null?Integer.toHexString(System.identityHashCode(getBuyer())):"null");
  }
}