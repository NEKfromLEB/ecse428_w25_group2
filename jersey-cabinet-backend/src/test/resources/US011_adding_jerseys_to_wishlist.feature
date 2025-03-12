Feature: Wishlist Functionality for Jerseys  

  As a Customer,  
  I want to be able to add jerseys to my wishlist  
  So that I can be notified once they are on sale.  

  Background:
    Given the customer is logged into their account

  Scenario: Customer adds a jersey to their wishlist  
    When the customer requests to add a jersey to their wishlist  
    Then the jersey is added to the customer's wishlist  
    And the customer receives a confirmation message  

  Scenario: Customer views their wishlist   
    When the customer requests to see their wishlist 
    Then the wishlist displays all previously added jerseys  

  Scenario: Customer is notified when a wishlist jersey goes on sale  
    Given the customer has added a jersey to their wishlist   
    When a sale begins for the jersey  
    Then the customer receives a notification about the discount  

