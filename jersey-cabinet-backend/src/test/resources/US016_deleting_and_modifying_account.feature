Feature: Modify/Delete Accounts of users
  As a user,  
  I want to modify or delete my account securely,  
  So that I can keep my information up to date or remove my account if needed.   

  Background:  
    Given the system has an account management platform  

  ## Normal Flows (Successful Modifications & Deletions)

  Scenario Outline: Successfully modify customer account  
    When the customer updates their email to "<new_email>", password to "<new_password>", and shipping address to "<new_address>"  
    Then the system updates their account information  
    And the customer's information is successfully modified

    Examples:  
      | new_email           | new_password | new_address             |  
      | mahmoud@email.com   | Pass123!     | 123 Main St, Montreal   |  
      | amin@email.com      | NewPass456   | 456 King St, Toronto    |  

  Scenario Outline: Successfully modify employee account  
    When the employee updates their email to "<new_email>" and password to "<new_password>"  
    Then the system updates their account information  

    Examples:  
      | new_email            | new_password  |  
      | namir@store.com      | Secure99      |  
      | habib@shop.com       | Pass12        |  

  Scenario Outline: Customer deletes their own account 
    When they enter their email "<email>" and password "<password>"  
    And they confirm account deletion  
    Then the system permanently deletes their account  

    Examples:  
      | email              | password   |  
      | mahmoud@email.com  | Pass123!   |  
      | amin@mail.com      | Secure456  |  

 ## Alternative Flows 

  Scenario Outline: Customer modifies only thier email or password or address
    When the customer updates either their email to "<new_email>" or password to "<new_password>" or address to "<new_address>"
    Then the system updates only the provided information successfully

    Examples:  
      | new_email         | new_password | new_address           |
      | mahmoud@email.com |              |                       |
      |                   | NewPass789   |                       |
      |                   |              | 123 Main St, Montreal |
 
  Scenario Outline: Employee modifies only their email or password  
    When the employee updates either their email to "<new_email>" or password to "<new_password>"  
    Then the system updates only the provided information successfully

    Examples:  
      | new_email            | new_password  |  
      | amin@store.com       |               |  
      |                      | SuperPass321  |  

  ## Error Flows  

Scenario Outline: Incomplete account deletion for client
  When the client requests to delete their account with "<email>" and "<password>"  
  Then the system prevents deletion if any of those fields are empty  
  And an error message is displayed: "Email and password are required to delete your account."  

  Examples:  
    | email                   | password  |  
    |                         | 123       |  
    | mahmoud@email.com       |           |  

Scenario Outline: Client tries to delete non-existent account  
  When the client requests to delete an account that does not exist with "<email>" and "<password>"  
  Then the system prevents deletion  
  And an error message is displayed: "Account not found. Please check your details and try again."  

  Examples:  
    | email                      | password   |  
    | unknown@email.com          | userpass   |  
    | deletedaccount@email.com   | mypassword |  


  Scenario Outline: Attempt to modify account with no fields 
    When the user tries to update their email to "<new_email>", password to "<new_password>", or shipping address to "<new_address>"  
    And all fields are empty  
    Then the system prevents the update  
    And an error message is displayed: "Please complete all required fields before submitting."  

    Examples:  
      | new_email         | new_password | new_address  |  
      |                   |              |              | 


 

