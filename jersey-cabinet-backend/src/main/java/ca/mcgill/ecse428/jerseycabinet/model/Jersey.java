package ca.mcgill.ecse428.jerseycabinet.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// line 28 "model.ump"
// line 73 "model.ump"
@Entity
public class Jersey
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum RequestState { Rejected, Unlisted, Listed, Bought }

  //-----------------------
  // MEMBER VARIABLES
  //-----------------------
  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Jersey Attributes
  private RequestState requestState;
  private String description;

  //Autounique Attributes
  @Id
  private int id;
  
  

  //Jersey Associations
  private Employee employee;
  private Customer seller;
  private List<Offer> offers;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Jersey(RequestState aRequestState, String aDescription, Employee aEmployee, Customer aSeller)
  {
    requestState = aRequestState;
    description = aDescription;
    id = nextId++;
    boolean didAddEmployee = setEmployee(aEmployee);
    if (!didAddEmployee)
    {
      throw new RuntimeException("Unable to create jersey due to employee. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddSeller = setSeller(aSeller);
    if (!didAddSeller)
    {
      throw new RuntimeException("Unable to create ToSell due to seller. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    offers = new ArrayList<Offer>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRequestState(RequestState aRequestState)
  {
    boolean wasSet = false;
    requestState = aRequestState;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public RequestState getRequestState()
  {
    return requestState;
  }

  public String getDescription()
  {
    return description;
  }

  public int getId(){
    return id;
  }
  /* Code from template association_GetOne */
  public Employee getEmployee()
  {
    return employee;
  }
  /* Code from template association_GetOne */
  public Customer getSeller()
  {
    return seller;
  }
  /* Code from template association_GetMany */
  public Offer getOffer(int index)
  {
    Offer aOffer = offers.get(index);
    return aOffer;
  }

  public List<Offer> getOffers()
  {
    List<Offer> newOffers = Collections.unmodifiableList(offers);
    return newOffers;
  }

  public int numberOfOffers()
  {
    int number = offers.size();
    return number;
  }

  public boolean hasOffers()
  {
    boolean has = offers.size() > 0;
    return has;
  }

  public int indexOfOffer(Offer aOffer)
  {
    int index = offers.indexOf(aOffer);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setEmployee(Employee aEmployee)
  {
    boolean wasSet = false;
    if (aEmployee == null)
    {
      return wasSet;
    }

    Employee existingEmployee = employee;
    employee = aEmployee;
    if (existingEmployee != null && !existingEmployee.equals(aEmployee))
    {
      existingEmployee.removeJersey(this);
    }
    employee.addJersey(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setSeller(Customer aSeller)
  {
    boolean wasSet = false;
    if (aSeller == null)
    {
      return wasSet;
    }

    Customer existingSeller = seller;
    seller = aSeller;
    if (existingSeller != null && !existingSeller.equals(aSeller))
    {
      existingSeller.removeToSell(this);
    }
    seller.addToSell(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOffers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Offer addOffer(Offer.OfferState aOfferState, int aPrice, Employee aEmployee)
  {
    return new Offer(aOfferState, aPrice, this, aEmployee);
  }

  public boolean addOffer(Offer aOffer)
  {
    boolean wasAdded = false;
    if (offers.contains(aOffer)) { return false; }
    Jersey existingJersey = aOffer.getJersey();
    boolean isNewJersey = existingJersey != null && !this.equals(existingJersey);
    if (isNewJersey)
    {
      aOffer.setJersey(this);
    }
    else
    {
      offers.add(aOffer);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOffer(Offer aOffer)
  {
    boolean wasRemoved = false;
    //Unable to remove aOffer, as it must always have a jersey
    if (!this.equals(aOffer.getJersey()))
    {
      offers.remove(aOffer);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOfferAt(Offer aOffer, int index)
  {  
    boolean wasAdded = false;
    if(addOffer(aOffer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOffers()) { index = numberOfOffers() - 1; }
      offers.remove(aOffer);
      offers.add(index, aOffer);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOfferAt(Offer aOffer, int index)
  {
    boolean wasAdded = false;
    if(offers.contains(aOffer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOffers()) { index = numberOfOffers() - 1; }
      offers.remove(aOffer);
      offers.add(index, aOffer);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOfferAt(aOffer, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Employee placeholderEmployee = employee;
    this.employee = null;
    if(placeholderEmployee != null)
    {
      placeholderEmployee.removeJersey(this);
    }
    Customer placeholderSeller = seller;
    this.seller = null;
    if(placeholderSeller != null)
    {
      placeholderSeller.removeToSell(this);
    }
    for(int i=offers.size(); i > 0; i--)
    {
      Offer aOffer = offers.get(i - 1);
      aOffer.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "description" + ":" + getDescription()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "requestState" + "=" + (getRequestState() != null ? !getRequestState().equals(this)  ? getRequestState().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "employee = "+(getEmployee()!=null?Integer.toHexString(System.identityHashCode(getEmployee())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "seller = "+(getSeller()!=null?Integer.toHexString(System.identityHashCode(getSeller())):"null");
  }
}