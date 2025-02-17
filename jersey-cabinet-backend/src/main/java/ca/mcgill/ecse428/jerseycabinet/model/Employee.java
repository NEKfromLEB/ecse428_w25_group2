package ca.mcgill.ecse428.jerseycabinet.model;

import java.util.*;

// line 11 "model.ump"
// line 58 "model.ump"
public class Employee extends Staff
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Employee Associations
  private List<Jersey> jerseies;
  private List<Offer> offers;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Employee(int aId, String aEmail, String aPassword)
  {
    super(aId, aEmail, aPassword);
    jerseies = new ArrayList<Jersey>();
    offers = new ArrayList<Offer>();
  }

  //------------------------
  // INTERFACE
  //------------------------
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfJerseies()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Jersey addJersey(Jersey.RequestState aRequestState, String aDescription, Customer aSeller)
  {
    return new Jersey(aRequestState, aDescription, this, aSeller);
  }

  public boolean addJersey(Jersey aJersey)
  {
    boolean wasAdded = false;
    if (jerseies.contains(aJersey)) { return false; }
    Employee existingEmployee = aJersey.getEmployee();
    boolean isNewEmployee = existingEmployee != null && !this.equals(existingEmployee);
    if (isNewEmployee)
    {
      aJersey.setEmployee(this);
    }
    else
    {
      jerseies.add(aJersey);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeJersey(Jersey aJersey)
  {
    boolean wasRemoved = false;
    //Unable to remove aJersey, as it must always have a employee
    if (!this.equals(aJersey.getEmployee()))
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOffers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Offer addOffer(Offer.OfferState aOfferState, int aPrice, Jersey aJersey)
  {
    return new Offer(aOfferState, aPrice, aJersey, this);
  }

  public boolean addOffer(Offer aOffer)
  {
    boolean wasAdded = false;
    if (offers.contains(aOffer)) { return false; }
    Employee existingEmployee = aOffer.getEmployee();
    boolean isNewEmployee = existingEmployee != null && !this.equals(existingEmployee);
    if (isNewEmployee)
    {
      aOffer.setEmployee(this);
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
    //Unable to remove aOffer, as it must always have a employee
    if (!this.equals(aOffer.getEmployee()))
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
    for(int i=jerseies.size(); i > 0; i--)
    {
      Jersey aJersey = jerseies.get(i - 1);
      aJersey.delete();
    }
    for(int i=offers.size(); i > 0; i--)
    {
      Offer aOffer = offers.get(i - 1);
      aOffer.delete();
    }
    super.delete();
  }

}