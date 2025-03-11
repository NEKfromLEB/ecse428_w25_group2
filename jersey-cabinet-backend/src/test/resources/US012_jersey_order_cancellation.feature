Feature: Customer Jersey Order Cancellation

  As a Customer,
  I want to be able to cancel a jersey order I bought within 24 hours
  so that I can change my mind about a purchase without penalty.

  Background:
    Given the store has a jersey order cancellation system
    And a customer account exists in the system
    And the customer has purchased jerseys

  Scenario: Customer successfully cancels an order within 24 hours
    Given I am logged in as a customer
    And I have purchased a jersey with ID "1001" less than 24 hours ago
    When I navigate to "My Orders" page
    And I select the order with jersey ID "1001"
    And I click the "Cancel Order" button
    Then I should see a confirmation message "Your order has been successfully cancelled."
    And the jersey's request state should be changed to "Listed"
    And I should receive a refund for the purchase

  Scenario: Customer attempts to cancel an order after 24 hours
    Given I am logged in as a customer
    And I have purchased a jersey with ID "1002" more than 24 hours ago
    When I navigate to "My Orders" page
    And I select the order with jersey ID "1002"
    And I click the "Cancel Order" button
    Then I should see an error message "Orders can only be cancelled within 24 hours of purchase."
    And the jersey's request state should remain "Bought"

  Scenario: Customer cancels multiple orders within 24 hours
    Given I am logged in as a customer
    And I have purchased the following jerseys less than 24 hours ago:
      | jersey_id | price |
      | 1003      | 150   |
      | 1004      | 200   |
    When I navigate to "My Orders" page
    And I select the order with jersey ID "1003"
    And I click the "Cancel Order" button
    And I select the order with jersey ID "1004"
    And I click the "Cancel Order" button
    Then I should see a confirmation message "Your orders have been successfully cancelled."
    And the jerseys' request states should be changed to "Listed"
    And I should receive refunds for both purchases

  Scenario: Customer attempts to cancel a non-existent order
    Given I am logged in as a customer
    When I navigate to "My Orders" page
    And I try to cancel an order with jersey ID "9999" that doesn't exist
    Then I should see an error message "Order not found."

  Scenario: Customer attempts to cancel an already cancelled order
    Given I am logged in as a customer
    And I have already cancelled an order with jersey ID "1005"
    When I navigate to "My Orders" page
    And I try to cancel the same order with jersey ID "1005" again
    Then I should see an error message "This order has already been cancelled."