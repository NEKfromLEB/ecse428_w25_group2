Feature: Purchase History

  As a buyer,
  I want to view my purchase history
  So that I can review the details of my purchases

  Scenario: View an empty purchase history
    Given I have 0 orders
    Then I should see a message "No order history"
    And no orders are listed

  Scenario: View my purchase history
    Given I have purchased 2 jerseys
    Then I should see a list of my orders
    And each item should display the jersey name, purchase date, price, and status
    And the list should be ordered by purchase date in descending order

  Scenario: View details of an order
    Given I have purchased a jersey with order ID 1
    Then I should see detailed information including:
      | Field           | Value          |
      | Jersey Name     | Lakers 2020    |
      | Description     | Basketball     |
      | Seller Info     | Address 123 St |
      | Purchase Date   | 2025-01-01     |
      | Price           | $80            |
      | Transaction ID  | 1              |

  Scenario: Filter order history by date range
    Given I have purchased 2 jerseys
    And the purchases were done on "2025-01-05" and "2025-03-01"
    When I apply a date filter from "2025-01-01" to "2025-02-01"
    Then I should see only the purchases made on "2025-01-05"

  Scenario: Filter by invalid date range
    Given I have purchased 2 jerseys
    And the purchases were done on "2025-01-05" and "2025-03-01"
    When I apply a date filter from "2025-03-01" to "2025-02-01"
    Then I should see a message "Invalid date range. End date must be after start date."
