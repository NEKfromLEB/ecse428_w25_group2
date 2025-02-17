package ca.mcgill.ecse428.jerseycabinet.model;

import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Staff extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------
  protected Staff(){
  }

  public Staff(int aId, String aEmail, String aPassword)
  {
    super(aId, aEmail, aPassword);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}