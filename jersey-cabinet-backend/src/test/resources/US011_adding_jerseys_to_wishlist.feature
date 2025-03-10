Feature: Wishlist Functionality for Jerseys  

  As a Customer,  
  I want to be able to add jerseys to my wishlist  
  So that I can be notified once they are on sale.  

  Scenario: Customer adds a jersey to their wishlist  
    Given the customer is logged into their account  
    And the customer is on the jersey product page  
    When the customer clicks the "Add to Wishlist" button  
    Then the jersey is added to the customer's wishlist  
    And the customer receives a confirmation message  

  Scenario: Customer views their wishlist  
    Given the customer is logged into their account  
    When the customer navigates to the "Wishlist" page  
    Then the wishlist displays all previously added jerseys  

  Scenario: Customer is notified when a wishlist jersey goes on sale  
    Given the customer has added a jersey to their wishlist  
    And the jersey's price is reduced  
    When a sale begins for the jersey  
    Then the customer receives a notification about the discount  

