Feature: Modify Employee Account Information

As an Employee,  
I want to modify my account details,  
So that I can update my email or password when needed.  

Background:  
    Given I am logged in as an Employee  

Scenario Outline: Successfully Modify Account Information (Normal Flow)  
    When I change my email to "<email>" and password to "<password>" 
    Then my account information is updated  

    Examples:  
    | email                  | password   |           
    | newemail@gmail.com     | NewPass123 |  

Scenario: Successfully Modify One Field at a Time (Alternative Flow)  
    Given I attempt to modify my email to "newemail@yahoo.com" and leave the password unchanged  
    When I submit the changes to email 
    Then my account information is updated with the new email  
    And no changes are made to the password  

Scenario: Successfully Modify Only One Field at a Time (Alternative Flow)  
    Given I attempt to modify my password to "NewPass514" and leave the email unchanged  
    When I submit the changes to password
    Then my account information is updated with the new password  
    And no changes are made to the email  

Scenario: Attempting to Modify a Non-Existent Account (Error Flow)  
    Given the employee account does not exist  
    When I try to modify the email to "invalidemail@gmail.com"  
    Then the system displays an error message: "Account not found."  
    And no changes are made
