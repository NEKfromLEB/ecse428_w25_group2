package ca.mcgill.ecse428.jerseycabinet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Jersey
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum RequestState { Rejected, Unlisted, Listed, Bought }
  public enum HomeAway { Home, Away }
  public enum Condition { BrandNew, LikeNew, Used }
  public enum Size { Small, Medium, Large, ExtraLarge }
  public enum AuthenticationApprovalStatus { Pending, Approved, Rejected }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Jersey Attributes
  @Id
  @GeneratedValue
  private int id;
  private RequestState requestState;
  private String description;
  private String brand;
  private String sport;
  private String color;
  private String jerseyImage;
  private String proofOfAuthenticationImage;
  private double salePrice;

  //Jersey Associations
  @ManyToOne
  private Employee employee;
  @ManyToOne
  private Customer seller;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public Jersey(){}

  public Jersey(RequestState aRequestState, String aDescription, String aBrand, String aSport, String aColor, String aJerseyImage, String aProofOfAuthenticationImage, double aSalePrice,  Customer aSeller)
  {
    requestState = aRequestState;
    description = aDescription;
    brand = aBrand;
    sport = aSport;
    color = aColor;
    jerseyImage = aJerseyImage;
    salePrice = aSalePrice;
    proofOfAuthenticationImage = aProofOfAuthenticationImage;
    if (!setSeller(aSeller))
    {
      throw new RuntimeException("Unable to create Jersey due to aSeller. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
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

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setBrand(String aBrand)
  {
    boolean wasSet = false;
    brand = aBrand;
    wasSet = true;
    return wasSet;
  }

  public boolean setSport(String aSport)
  {
    boolean wasSet = false;
    sport = aSport;
    wasSet = true;
    return wasSet;
  }

  public boolean setColor(String aColor)
  {
    boolean wasSet = false;
    color = aColor;
    wasSet = true;
    return wasSet;
  }

  public boolean setJerseyImage(String aJerseyImage)
  {
    boolean wasSet = false;
    jerseyImage = aJerseyImage;
    wasSet = true;
    return wasSet;
  }

  public boolean setProofOfAuthenticationImage(String aProofOfAuthenticationImage)
  {
    boolean wasSet = false;
    proofOfAuthenticationImage = aProofOfAuthenticationImage;
    wasSet = true;
    return wasSet;
  }

  public boolean setSalePrice(double aSalePrice)
  {
    boolean wasSet = false;
    salePrice = aSalePrice;
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

  public int getId()
  {
    return id;
  }

  public String getBrand()
  {
    return brand;
  }

  public String getSport()
  {
    return sport;
  }

  public String getColor()
  {
    return color;
  }

  public String getJerseyImage()
  {
    return jerseyImage;
  }

  public String getProofOfAuthenticationImage()
  {
    return proofOfAuthenticationImage;
  }

  public double getSalePrice(){
    return salePrice;
  }
  /* Code from template association_GetOne */
  public Employee getEmployee()
  {
    return employee;
  }

  public boolean hasEmployee(){
    boolean has = employee != null;
    return has;
  }
 //Code from template association_SetUnidirectionalOptionalOne
  public boolean setEmployee(Employee aNewEmployee){
    boolean wasSet = false;
    employee = aNewEmployee;
    wasSet = true;
    return wasSet;
  }  
  /* Code from template association_GetOne */
  public Customer getSeller()
  {
    return seller;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setSeller(Customer aNewSeller)
  {
    boolean wasSet = false;
    if (aNewSeller != null)
    {
      seller = aNewSeller;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    employee = null;
    seller = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "description" + ":" + getDescription()+ "," +
            "id" + ":" + getId()+ "," +
            "brand" + ":" + getBrand()+ "," +
            "sport" + ":" + getSport()+ "," +
            "color" + ":" + getColor()+ "," +
            "jerseyImage" + ":" + getJerseyImage()+ "," +
            "proofOfAuthenticationImage" + ":" + getProofOfAuthenticationImage()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "requestState" + "=" + (getRequestState() != null ? !getRequestState().equals(this)  ? getRequestState().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "employee = "+(getEmployee()!=null?Integer.toHexString(System.identityHashCode(getEmployee())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "seller = "+(getSeller()!=null?Integer.toHexString(System.identityHashCode(getSeller())):"null");
  }
}