Feature: Counter Offer

As an Employee,
I want to be able to counter a rejected offer
So that the Customer can review the offer again.

Background:
    Given the following offer exist in the system
        | price | offerState | jerseyId | employeeId |
        | 100   | rejected   | 20       | 34         |
        | 150   | accepted   | 21       | 35         |
    
    And I am logged in as an Employee

Scenario Outline: Successfully counter a rejected offer (Normal Flow)
    When I enter a new price for the offer
    And I confirm the update of the offer
    Then the offer state becomes pending
    And the price is updated to the new price

Scenario Outline: Unsuccessfully counter an accepted offer (Error Flow)
    When I attempt to counter an accepted offer
    Then the system displays an error message "Accepted offers cannot be modified."

Scenario Outline: Unsuccessfully counter an offer because of invalid price (negative) (Error Flow)
    When I enter a new negative price for the offer
    And I confirm the update of the offer
    Then the system displays an error message "Please enter a non-negative offer amount."