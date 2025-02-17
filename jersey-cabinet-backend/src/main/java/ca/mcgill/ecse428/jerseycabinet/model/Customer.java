package ca.mcgill.ecse428.jerseycabinet.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

// line 20 "model.ump"
// line 68 "model.ump"
public class Customer extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Customer Associations
  private List<Jersey> ToSell;
  private Cart ToBuy;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Customer(int aId, String aEmail, String aPassword, Cart aToBuy)
  {
    super(aId, aEmail, aPassword);
    ToSell = new ArrayList<Jersey>();
    if (aToBuy == null || aToBuy.getBuyer() != null)
    {
      throw new RuntimeException("Unable to create Customer due to aToBuy. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    ToBuy = aToBuy;
  }

  public Customer(int aId, String aEmail, String aPassword, int aTotalPriceForToBuy)
  {
    super(aId, aEmail, aPassword);
    ToSell = new ArrayList<Jersey>();
    ToBuy = new Cart(aTotalPriceForToBuy, this);
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public Jersey getToSell(int index)
  {
    Jersey aToSell = ToSell.get(index);
    return aToSell;
  }

  public List<Jersey> getToSell()
  {
    List<Jersey> newToSell = Collections.unmodifiableList(ToSell);
    return newToSell;
  }

  public int numberOfToSell()
  {
    int number = ToSell.size();
    return number;
  }

  public boolean hasToSell()
  {
    boolean has = ToSell.size() > 0;
    return has;
  }

  public int indexOfToSell(Jersey aToSell)
  {
    int index = ToSell.indexOf(aToSell);
    return index;
  }
  /* Code from template association_GetOne */
  public Cart getToBuy()
  {
    return ToBuy;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfToSell()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Jersey addToSell(Jersey.RequestState aRequestState, String aDescription, Employee aEmployee)
  {
    return new Jersey(aRequestState, aDescription, aEmployee, this);
  }

  public boolean addToSell(Jersey aToSell)
  {
    boolean wasAdded = false;
    if (ToSell.contains(aToSell)) { return false; }
    Customer existingSeller = aToSell.getSeller();
    boolean isNewSeller = existingSeller != null && !this.equals(existingSeller);
    if (isNewSeller)
    {
      aToSell.setSeller(this);
    }
    else
    {
      ToSell.add(aToSell);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeToSell(Jersey aToSell)
  {
    boolean wasRemoved = false;
    //Unable to remove aToSell, as it must always have a seller
    if (!this.equals(aToSell.getSeller()))
    {
      ToSell.remove(aToSell);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addToSellAt(Jersey aToSell, int index)
  {  
    boolean wasAdded = false;
    if(addToSell(aToSell))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfToSell()) { index = numberOfToSell() - 1; }
      ToSell.remove(aToSell);
      ToSell.add(index, aToSell);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveToSellAt(Jersey aToSell, int index)
  {
    boolean wasAdded = false;
    if(ToSell.contains(aToSell))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfToSell()) { index = numberOfToSell() - 1; }
      ToSell.remove(aToSell);
      ToSell.add(index, aToSell);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addToSellAt(aToSell, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=ToSell.size(); i > 0; i--)
    {
      Jersey aToSell = ToSell.get(i - 1);
      aToSell.delete();
    }
    Cart existingToBuy = ToBuy;
    ToBuy = null;
    if (existingToBuy != null)
    {
      existingToBuy.delete();
    }
    super.delete();
  }

}