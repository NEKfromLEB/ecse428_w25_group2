package ca.mcgill.ecse428.jerseycabinet.model;

import jakarta.persistence.Entity;

@Entity
public class Manager extends Staff
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------
  protected Manager(){
  }

  public Manager(int aId, String aEmail, String aPassword)
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