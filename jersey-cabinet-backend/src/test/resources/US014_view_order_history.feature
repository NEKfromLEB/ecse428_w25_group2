Feature: Purchase History

  As a buyer,
  I want to view my purchase history
  So that I can review the details of my purchases

  Scenario: View an empty purchase history
    Given I have purchased 0 jerseys
    When I request to view my purchase history
    Then I should see an error message "No order history"
    And no orders are listed

  Scenario: View my purchase history
    Given I have purchased 2 jerseys
    When I request to view my purchase history
    Then I should see a list of my 2 orders
    And the list should be ordered by purchase date in descending order

  Scenario: Filter order history by date range
    Given I have purchased 2 jerseys
    And the purchases were done on "2025-01-05" and "2025-03-01"
    When I apply a date filter from "2025-01-01" to "2025-02-01"
    Then I should see only the purchases made on "2025-01-05"

  Scenario: Filter by invalid date range
    Given I have purchased 2 jerseys
    And the purchases were done on "2025-01-05" and "2025-03-01"
    When I apply a date filter from "2025-03-01" to "2025-02-01"
    Then I should see an error message "Invalid date range. End date must be after start date."

  Scenario: No order in date range
    Given I have purchased 2 jerseys
    And the purchases were done on "2025-01-05" and "2025-03-01"
    When I apply a date filter from "2025-05-01" to "2025-10-01"
    Then I should see an error message "No orders between 2025-05-01 and 2025-10-01"
