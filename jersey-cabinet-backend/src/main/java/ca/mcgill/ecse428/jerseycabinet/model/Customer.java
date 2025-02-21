package ca.mcgill.ecse428.jerseycabinet.model;

import jakarta.persistence.Entity;

@Entity
public class Customer extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Customer Attributes
  private String shippingAddress;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public Customer() {}

  public Customer(String aEmail, String aPassword, String aShippingAddress)
  {
    super(aEmail, aPassword);
    shippingAddress = aShippingAddress;
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

  public void delete()
  {
    super.delete();
  }
}