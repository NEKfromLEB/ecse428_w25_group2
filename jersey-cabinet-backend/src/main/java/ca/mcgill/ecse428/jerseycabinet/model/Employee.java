package ca.mcgill.ecse428.jerseycabinet.model;

import jakarta.persistence.Entity;

@Entity
public class Employee extends Staff
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public Employee(){}

  public Employee(String aEmail, String aPassword)
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