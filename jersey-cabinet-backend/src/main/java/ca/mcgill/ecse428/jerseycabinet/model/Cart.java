package ca.mcgill.ecse428.jerseycabinet.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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
  private List<Jersey> jerseies;
  private Customer buyer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Cart(int aTotalPrice, Customer aBuyer)
  {
    totalPrice = aTotalPrice;
    jerseies = new ArrayList<Jersey>();
    if (aBuyer == null || aBuyer.getToBuy() != null)
    {
      throw new RuntimeException("Unable to create Cart due to aBuyer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    buyer = aBuyer;
  }

  public Cart(int aTotalPrice, int aIdForBuyer, String aEmailForBuyer, String aPasswordForBuyer)
  {
    totalPrice = aTotalPrice;
    jerseies = new ArrayList<Jersey>();
    buyer = new Customer(aIdForBuyer, aEmailForBuyer, aPasswordForBuyer, this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTotalPrice(int aTotalPrice)
  {
    boolean wasSet = false;
    totalPrice = aTotalPrice;
    wasSet = true;
    return wasSet;
  }

  public int getTotalPrice()
  {
    return totalPrice;
  }
  /* Code from template association_GetMany */
  public Jersey getJersey(int index)
  {
    Jersey aJersey = jerseies.get(index);
    return aJersey;
  }

  public List<Jersey> getJerseies()
  {
    List<Jersey> newJerseies = Collections.unmodifiableList(jerseies);
    return newJerseies;
  }

  public int numberOfJerseies()
  {
    int number = jerseies.size();
    return number;
  }

  public boolean hasJerseies()
  {
    boolean has = jerseies.size() > 0;
    return has;
  }

  public int indexOfJersey(Jersey aJersey)
  {
    int index = jerseies.indexOf(aJersey);
    return index;
  }
  /* Code from template association_GetOne */
  public Customer getBuyer()
  {
    return buyer;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfJerseies()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addJersey(Jersey aJersey)
  {
    boolean wasAdded = false;
    if (jerseies.contains(aJersey)) { return false; }
    jerseies.add(aJersey);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeJersey(Jersey aJersey)
  {
    boolean wasRemoved = false;
    if (jerseies.contains(aJersey))
    {
      jerseies.remove(aJersey);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addJerseyAt(Jersey aJersey, int index)
  {  
    boolean wasAdded = false;
    if(addJersey(aJersey))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfJerseies()) { index = numberOfJerseies() - 1; }
      jerseies.remove(aJersey);
      jerseies.add(index, aJersey);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveJerseyAt(Jersey aJersey, int index)
  {
    boolean wasAdded = false;
    if(jerseies.contains(aJersey))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfJerseies()) { index = numberOfJerseies() - 1; }
      jerseies.remove(aJersey);
      jerseies.add(index, aJersey);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addJerseyAt(aJersey, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    jerseies.clear();
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
            "totalPrice" + ":" + getTotalPrice()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "buyer = "+(getBuyer()!=null?Integer.toHexString(System.identityHashCode(getBuyer())):"null");
  }
}