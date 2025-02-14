Feature: Fill Out Card Information for Jersey Purchase
As a user,  
I want to be able to fill out my card information to purchase a jersey, so that I can complete my transaction securely.

  Background:  
    Given the store has an online checkout system  
    And a user has added a jersey to their cart  

  Scenario: Successfully Fill Out and Submit Card Information  
    When the user fills out the card information with card number "<cardNumber>", expiration date "<expirationDate>", CVV "<cvv>", and cardholder name "<cardholderName>"  
    And the user submits the payment form  
    Then the system shall confirm the payment and display a success message  
    And the order shall be recorded in the system  
    Examples:  
      | cardNumber       | expirationDate | cvv | cardholderName   |  
      | 0000000000000001 | 12/25          | 123 |  Rami Nawam      |  
      | 0000000000000002 | 06/26          | 456 | Rudy Itani       |  
      | 0000000000000003 | 09/24          | 789 | David Margi      |  

  Scenario Outline: Fill Out Form with Missing Card Details (Alternative Flow)  
    When the user fills out the card information with card number "<cardNumber>", expiration date "<expirationDate>", CVV "<cvv>", and cardholder name "<cardholderName>"  
    And any field is missing  
    Then the system warns: "Some payment details are missing. Please complete all required fields."  
    And the user is unable to proceed with the payment  
    Examples:  
      | cardNumber       | expirationDate | cvv | cardholderName |  
      |                  | 12/25          | 123 | John Doe       |  
      | 5500000000000004 |                | 456 | Jane Smith     |  
      | 340000000000009  | 09/24          |     | Alex Johnson   |  

  Scenario: Error - Invalid Card Information (Error flow)  
    When the user enters invalid card information with card number "<cardNumber>", expiration date "<expirationDate>", CVV "<cvv>", or cardholder name "<cardholderName>"  
    Then the system prevents submission  
    And an error message is displayed: "Invalid card details. Please try again."  
    And the form remains on the page for correction  
    Examples:  
      | cardNumber       | expirationDate | cvv  | cardholderName |  
      | 1234567890123456 | 13/25          | 123  | John Doe       |  
      | 5500000000000004 | 06/10          | 456  | Jane Smith     |  
      | ABCDEFGHIJKLMNOP | 09/24          | 789  | Alex Johnson   |  
