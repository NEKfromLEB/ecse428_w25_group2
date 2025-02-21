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
  public Manager(){}
  
  public Manager(String aEmail, String aPassword)
  {
    super(aEmail, aPassword);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}