Feature: Modify/Delete Card Information

As a Customer, 
I want to be able to delete/modify my card information 
So that I can keep my preferences updated.

Background:
    Given I am logged in as a Customer 
    And the following card is registered to me
    | billingAddress   | cardHolderName | cardNumber       | cardCVV | cardExpiryDate
    | 100 Main Street  | Namir Habib    | 0000000000000001 |  122    | 03/27

    
Scenario Outline: Successfully modify card information (Normal Flow)
    When I modify a "<field>" with "<new_information>"
    Then the card information is updated to reflect the change.

    Examples:
    | field                      | new_information  | 
    | billingAddress             | 100 Sub Street   |
    | cardHolderName             | Justin Trudeau   |
    | cardNumber                 | 300              | 
    | cardCVV                    | 800              |
    | cardExpiryDate             | 09/23            |         
Scenario: I successfully delete a card (Normal Flow)
    When I request to delete the card registered to my file 
    Then the card is deleted
Scenario Outline: Unsuccessfully delete the information of a field and leave it blank (Error flow)
    When I leave a "<field>" blank
    Then the system warns me that the card information is invalid    
    And the form is not updated
    
    Examples:
    | field                      | 
    | billingAddress             |                  
    | cardHolderName             |                  
    | cardNumber                 |                   
    | cardCVV                    |                  
    | cardExpiryDate             |                  

Scenario Outline: Unsuccessully input an invalid date (Error Flow)
    When I enter an '<invalid date>' 
    Then the system warns me that the card information is invalid
    And the form is not updated.
    
    Examples:
    |invalid date|
    | aaaa       |
    | 2025       |
    | 15/98      |
