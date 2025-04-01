Feature: Modify/Delete Accounts of Manager
As a manager,  
I want to modify or delete the account of clients and employees,  
So that I can keep their information up to date or remove their account if needed.  

  Background:  
    Given the system has an account management platform  

  ## Normal Flows (Successful Modifications & Deletions)

   Scenario Outline: Manager modifies their own email and password  
    Given the manager is logged in  
    When the manager updates their email to "<new_manager_email>" and their password to "<new_manager_password>"
    Then the system updates the user's email  

    Examples:  
      | new_manager_email         | new_manager_password  |
      | mahmoud@manager.com       | MoSalah123            | 
      | amin@manager.com          | YNWA2003              | 

  Scenario Outline: Manager modifies another user’s email and password
    Given the manager is logged in  
    When the manager updates the user's email to "<new_user_email>" and their password to "<new_user_password>"
    Then the system updates the user's email and password

    Examples:  
      | new_user_email         | new_user_password     |
      | namir@user.com         | ColePalmer123         | 
      | habib@user.com         | StamfordBridge2003    | 

  Scenario Outline: Manager deletes their own account  
    Given the customer or manager requests deletion  
    When they enter their email "<email>" and password "<password>"  
    And they confirm account deletion  
    Then the system permanently deletes their account  

    Examples:  
      | email                 | password    |  
      | mahmoud@manager.com   | MoSalah123  |  
      | amin@manager.com      | YNWA2003    |  

   Scenario Outline: Manager deletes a user's account  
     Given the manager requests deletion  
     When they enter the user's email "<email>" and password "<password>"  
     And they confirm account deletion  
     Then the system permanently deletes their account  

    Examples:  
      | email               | password   |  
      | mahmoud@email.com   | Pass123!   |  
      | amin@email.com      | Secure456  |  

 ## Alternative Flows 

  Scenario Outline: Manager modifies their email or password 
    When the customer updates either their email to "<new_manager_email>" or password to "<new_manager_password>" 
    Then the system updates only the provided information successfully

    Examples:  
      | new_manager_email               | new_manager_password | 
      | mahmoudAmin@manager.com         |                      |    
      |                                 | YNWA2003             | 

Scenario Outline: Manager modifies a user's email or password 
    When the customer updates either their email to "<new_user_email>" or password to "<new_user_password>" 
    Then the system updates only the provided information successfully

    Examples:  
      | new_user_email      | new_user_password         | 
      | moSalah11@user.com  |                           |    
      |                     | AnfieldIsAFortress        | 

  ## Error Flows 

Scenario Outline: Incomplete information to delete manager
  Given the user is a manager  
  When the manager requests to delete their account with "<email>" and "<password>"  
  Then the system prevents deletion if any of those fields are empty  
  And an error message is displayed: "Email and password are required to delete an account."  
  And the form remains on the page for correction  

  Examples:  
    | email                     | password        |  
    |                           | managerPassword |  
    | angela@manager.com        |                 |  

Scenario Outline: Manager tries to delete non-existent account  
  When the manager requests to delete an account that does not exist with "<email>" and "<password>"  
  Then the system prevents deletion  
  And an error message is displayed: "Account not found. Please check your details and try again."  

  Examples:  
    | email                      | password   |  
    | unknown@email.com          | userpass   |  
    | deletedaccount@email.com   | mypassword |  


  Scenario Outline: Attempt to modify account with no fields 
    When the manager tries to update their email to "<new_email>", password to "<new_password>"  
    And all fields are empty  
    Then the system prevents the update  
    And an error message is displayed: "Please complete all required fields before submitting."  

    Examples:  
      | new_email         | new_password |   
      |                   |              |                
      

 

