package ca.mcgill.ecse428.jerseycabinet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToOne;

@Entity
public class Wishlist
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Wishlist Attributes
  @Id
  @GeneratedValue
  private int id;
  private String keywords;

  //Wishlist Associations
  @OneToOne
  private Customer customer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Wishlist(String aKeywords, Customer aCustomer)
  {
    keywords = aKeywords;
    if (aCustomer == null || aCustomer.getWishlist() != null)
    {
      throw new RuntimeException("Unable to create Wishlist due to aCustomer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    customer = aCustomer;
  }

  public Wishlist(String aKeywords, String aEmailForCustomer, String aPasswordForCustomer, String aShippingAddressForCustomer, Cart aToBuyForCustomer)
  {
    keywords = aKeywords;
    customer = new Customer(aEmailForCustomer, aPasswordForCustomer, aShippingAddressForCustomer, this, aToBuyForCustomer);
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

  public boolean setKeywords(String aKeywords)
  {
    boolean wasSet = false;
    keywords = aKeywords;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }

  public String getKeywords()
  {
    return keywords;
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }

  public void delete()
  {
    Customer existingCustomer = customer;
    customer = null;
    if (existingCustomer != null)
    {
      existingCustomer.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "keywords" + ":" + getKeywords()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null");
  }
}