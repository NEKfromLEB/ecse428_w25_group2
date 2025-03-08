Feature: Remove offer

As an Employee,
I want to be able to remove and unwanted offer
So that I can place a new offer

Background:
    Given the following offers exist in the system
        | price | offerState | jerseyId | employeeId |
        | 100   | rejected   | 20       | 34         |
        | 175   | pending    | 21       | 35         |
        | 130   | accepted   | 22       | 36         |
    
    And I am logged in as an Employee

Scenario Outline: Successfully remove a rejected offer (Normal Flow)
    When I confirm the removal of a rejected offer
    Then the offer is removed from the system

Scenario Outline: Successfully remove a pending offer (Alternate Flow)
    When I confirm the removal of a pending offer
    Then the offer is removed from the system

Scenario Outline: Unsuccessfully remove an offer that has already been accepted (Error Flow)
    When I attempt to remove an accepted offer
    Then the system displays an error message "Accepted offers cannot be removed."