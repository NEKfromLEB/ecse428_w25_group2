Feature: Counter Offer

As an Employee,
I want to be able to counter a rejected offer
So that the Customer can review the offer again.

Background:
    Given the following offers exist in the system
        | price | offerState | jerseyId | employeeId |
        | 100   | rejected   | 20       | 34         |
        | 150   | accepted   | 21       | 35         |
    
    And I am logged in as an Employee


Scenario Outline: US018: Successfully counter a rejected offer (Normal Flow)
    When I enter a <new_price> for the offer
    And I confirm the update of the offer
    Then the offer state becomes pending
    And the price is updated to the new price

    Examples:
        | new_price | offerState |
        | 125       | pending    |

Scenario Outline: US018: Unsuccessfully counter an accepted offer (Error Flow)
    When I attempt to counter an accepted offer
    Then the system warns the user that accepted offers cannot be modified
    And the offer state remains accepted

Scenario Outline: US018: Unsuccessfully counter an offer because of invalid price (negative) (Error Flow)
    When I enter a negative <new_price> for the offer
    And I confirm the update of the offer
    Then the system warns the user to enter a non-negative offer amount
    And the offer state remains unchanged

    Examples:
        | new_price | offerState |
        | -100      | accepted   | 
