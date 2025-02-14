Feature: Employee Jersey Offer Submission

  As an employee,
  I want to be able to put down an offer
  so that the Seller can decide if they want to sell it to me.

  Background:
    Given the store has a jersey offer submission system
    And a jersey offer exists in the system
    And an employee account exists

  Scenario Outline: Employee successfully submits an offer
    Given I am logged in as an employee
    And I have navigated to the page for the jersey offer with ID "<jersey_offer_id>"
    When I enter "<offer_amount>" as my offer amount
    And I click the "Submit Offer" button
    Then I should see a confirmation message "Your offer of $<offer_amount> has been submitted."
    And the seller should receive a notification about my offer

    Examples:
      | jersey_offer_id | offer_amount |
      | 1001            | 150          |
      | 1002            | 200          |

  # Error Flow: Invalid Offer Amount
  Scenario: Employee submits an invalid offer amount (negative)
    Given I am logged in as an employee
    And I have navigated to the page for the jersey offer with ID "1003"
    When I enter "-50" as my offer amount
    And I click the "Submit Offer" button
    Then I should see an error message "Please enter a non-negative offer amount."

  # Error Flow: Non-numeric Offer Input
  Scenario: Employee submits an offer with non-numeric input
    Given I am logged in as an employee
    And I have navigated to the page for the jersey offer with ID "1004"
    When I enter "abc" as my offer amount
    And I click the "Submit Offer" button
    Then I should see an error message "Please enter a numeric offer amount."