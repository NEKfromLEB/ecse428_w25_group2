package ca.mcgill.ecse428.jerseycabinet.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class PaymentMethod
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PaymentMethod Attributes
  @Id
  @GeneratedValue
  private int id;
  private String billingAddress;
  private String cardName;
  private String cardNumber;
  private String cardCVV;
  private Date cardExpiryDate;

  //PaymentMethod Associations
  @ManyToOne
  private Customer customer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentMethod(String aBillingAddress, String aCardName, String aCardNumber, String aCardCVV, Date aCardExpiryDate, Customer aCustomer)
  {
    billingAddress = aBillingAddress;
    cardName = aCardName;
    cardNumber = aCardNumber;
    cardCVV = aCardCVV;
    cardExpiryDate = aCardExpiryDate;
    if (!setCustomer(aCustomer))
    {
      throw new RuntimeException("Unable to create PaymentMethod due to aCustomer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
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

  public boolean setBillingAddress(String aBillingAddress)
  {
    boolean wasSet = false;
    billingAddress = aBillingAddress;
    wasSet = true;
    return wasSet;
  }

  public boolean setCardName(String aCardName)
  {
    boolean wasSet = false;
    cardName = aCardName;
    wasSet = true;
    return wasSet;
  }

  public boolean setCardNumber(String aCardNumber)
  {
    boolean wasSet = false;
    cardNumber = aCardNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setCardCVV(String aCardCVV)
  {
    boolean wasSet = false;
    cardCVV = aCardCVV;
    wasSet = true;
    return wasSet;
  }

  public boolean setCardExpiryDate(Date aCardExpiryDate)
  {
    boolean wasSet = false;
    cardExpiryDate = aCardExpiryDate;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }

  public String getBillingAddress()
  {
    return billingAddress;
  }

  public String getCardName()
  {
    return cardName;
  }

  public String getCardNumber()
  {
    return cardNumber;
  }

  public String getCardCVV()
  {
    return cardCVV;
  }

  public Date getCardExpiryDate()
  {
    return cardExpiryDate;
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCustomer(Customer aNewCustomer)
  {
    boolean wasSet = false;
    if (aNewCustomer != null)
    {
      customer = aNewCustomer;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    customer = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "billingAddress" + ":" + getBillingAddress()+ "," +
            "cardName" + ":" + getCardName()+ "," +
            "cardNumber" + ":" + getCardNumber()+ "," +
            "cardCVV" + ":" + getCardCVV()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "cardExpiryDate" + "=" + (getCardExpiryDate() != null ? !getCardExpiryDate().equals(this)  ? getCardExpiryDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null");
  }
}