package ca.mcgill.ecse428.jerseycabinet.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/



// line 41 "model.ump"
// line 85 "model.ump"
public class Offer
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum OfferState { Accepted, Pending, Rejected }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Offer Attributes
  private OfferState offerState;
  private int price;

  //Offer Associations
  private Jersey jersey;
  private Employee employee;

  //Helper Variables
  private int cachedHashCode;
  private boolean canSetJersey;
  private boolean canSetEmployee;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Offer(OfferState aOfferState, int aPrice, Jersey aJersey, Employee aEmployee)
  {
    cachedHashCode = -1;
    canSetJersey = true;
    canSetEmployee = true;
    offerState = aOfferState;
    price = aPrice;
    boolean didAddJersey = setJersey(aJersey);
    if (!didAddJersey)
    {
      throw new RuntimeException("Unable to create offer due to jersey. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddEmployee = setEmployee(aEmployee);
    if (!didAddEmployee)
    {
      throw new RuntimeException("Unable to create offer due to employee. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setOfferState(OfferState aOfferState)
  {
    boolean wasSet = false;
    offerState = aOfferState;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(int aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public OfferState getOfferState()
  {
    return offerState;
  }

  public int getPrice()
  {
    return price;
  }
  /* Code from template association_GetOne */
  public Jersey getJersey()
  {
    return jersey;
  }
  /* Code from template association_GetOne */
  public Employee getEmployee()
  {
    return employee;
  }
  /* Code from template association_SetOneToManyAssociationClass */
  public boolean setJersey(Jersey aJersey)
  {
    boolean wasSet = false;
    if (!canSetJersey) { return false; }
    if (aJersey == null)
    {
      return wasSet;
    }

    Jersey existingJersey = jersey;
    jersey = aJersey;
    if (existingJersey != null && !existingJersey.equals(aJersey))
    {
      existingJersey.removeOffer(this);
    }
    if (!jersey.addOffer(this))
    {
      jersey = existingJersey;
      wasSet = false;
    }
    else
    {
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetOneToManyAssociationClass */
  public boolean setEmployee(Employee aEmployee)
  {
    boolean wasSet = false;
    if (!canSetEmployee) { return false; }
    if (aEmployee == null)
    {
      return wasSet;
    }

    Employee existingEmployee = employee;
    employee = aEmployee;
    if (existingEmployee != null && !existingEmployee.equals(aEmployee))
    {
      existingEmployee.removeOffer(this);
    }
    if (!employee.addOffer(this))
    {
      employee = existingEmployee;
      wasSet = false;
    }
    else
    {
      wasSet = true;
    }
    return wasSet;
  }

  public boolean equals(Object obj)
  {
    if (obj == null) { return false; }
    if (!getClass().equals(obj.getClass())) { return false; }

    Offer compareTo = (Offer)obj;
  
    if (getJersey() == null && compareTo.getJersey() != null)
    {
      return false;
    }
    else if (getJersey() != null && !getJersey().equals(compareTo.getJersey()))
    {
      return false;
    }

    if (getEmployee() == null && compareTo.getEmployee() != null)
    {
      return false;
    }
    else if (getEmployee() != null && !getEmployee().equals(compareTo.getEmployee()))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    if (cachedHashCode != -1)
    {
      return cachedHashCode;
    }
    cachedHashCode = 17;
    if (getJersey() != null)
    {
      cachedHashCode = cachedHashCode * 23 + getJersey().hashCode();
    }
    else
    {
      cachedHashCode = cachedHashCode * 23;
    }
    if (getEmployee() != null)
    {
      cachedHashCode = cachedHashCode * 23 + getEmployee().hashCode();
    }
    else
    {
      cachedHashCode = cachedHashCode * 23;
    }

    canSetJersey = false;
    canSetEmployee = false;
    return cachedHashCode;
  }

  public void delete()
  {
    Jersey placeholderJersey = jersey;
    this.jersey = null;
    if(placeholderJersey != null)
    {
      placeholderJersey.removeOffer(this);
    }
    Employee placeholderEmployee = employee;
    this.employee = null;
    if(placeholderEmployee != null)
    {
      placeholderEmployee.removeOffer(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "price" + ":" + getPrice()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "offerState" + "=" + (getOfferState() != null ? !getOfferState().equals(this)  ? getOfferState().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "jersey = "+(getJersey()!=null?Integer.toHexString(System.identityHashCode(getJersey())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "employee = "+(getEmployee()!=null?Integer.toHexString(System.identityHashCode(getEmployee())):"null");
  }
}