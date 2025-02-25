Feature: Review Order List
  As an Employee
  I want to see a list of all orders and their status
  So that I can ensure their completion

  Scenario: View all orders (Normal flow)
   Given the following orders exist in the system
     | Customer Email | Jersey Description | Order Date | Status      |
     | mohamed@gmail.com         | Bulls     | 2025-02-21 | ToBeShipped |
     | tia@gmail.com         | Lakers     | 2025-02-22 | Shipped     |
     | moe@gmail.com         | Byers     | 2025-02-23 | Delivered   |
    When I navigate to the orders page
    Then I see a list of all orders with their status:
      | Customer Email          | Jersey Description      | Order Date | Status      |
      | mohamed@gmail.com    | Bulls        | 2025-02-21 | ToBeShipped |
      | tia@gmail.com  | Lakers       | 2025-02-22 | Shipped     |
      |moe@gmail.com  | Byers       | 2025-02-23 | Delivered   |

  Scenario: Filter orders by status
    Given there are certain orders in the system:
      | Customer Email    | Jersey Description | Order Date | Status      |
      | mohamed@gmail.com | Bulls              | 2025-02-21 | ToBeShipped |
      | tia@gmail.com     | Lakers             | 2025-02-22 | Shipped     |
      | moe@gmail.com     | Byers              | 2025-02-23 | Delivered   |
    When I filter orders by a specific status "Shipped"
    Then I only see orders that match the selected status:
      | Customer Email    | Jersey Description | Order Date | Status  |
      | tia@gmail.com     | Lakers             | 2025-02-22 | Shipped |

  Scenario Outline: Search for an order (Normal flow)
    Given there are certain orders in the system:
      | Customer Email    | Jersey Description | Order Date | Status      |
      | mohamed@gmail.com | Bulls              | 2025-02-21 | ToBeShipped |
      | tia@gmail.com     | Lakers             | 2025-02-22 | Shipped     |
      | moe@gmail.com     | Byers              | 2025-02-23 | Delivered   |
    When I search for an order using "<Customer Email>" and "<Jersey Description>"
    Then I see the relevant order in the list:
      | Customer Email    | Jersey Description | Order Date | Status   |
      | <Customer Email>  | <Jersey Description> | <Order Date> | <Status> |
    Examples:
      | Customer Email    | Jersey Description | Order Date | Status      |
      | mohamed@gmail.com | Bulls              | 2025-02-21 | ToBeShipped |
      | tia@gmail.com     | Lakers             | 2025-02-22 | Shipped     |
      | moe@gmail.com     | Byers              | 2025-02-23 | Delivered   |



  Scenario Outline: Search for an order that does not exist (Exception flow)
    Given there are certain orders in the system:
      | Customer Email    | Jersey Description | Order Date | Status      |
      | mohamed@gmail.com | Bulls              | 2025-02-21 | ToBeShipped |
      | tia@gmail.com     | Lakers             | 2025-02-22 | Shipped     |
      | moe@gmail.com     | Byers              | 2025-02-23 | Delivered   |
    When I search for an order using "<Customer Email>" and "<Jersey Description>"
    Then I see a message indicating "No orders found matching 'Customer Email: <Customer Email>, Jersey Description: <Jersey Description>'"
    Examples:
      | Customer Email      | Jersey Description |
      | unknown@gmail.com   | Bulls              |
      | tia@gmail.com       | Warriors           |
      | nonexistent@xyz.com | Nets               |





  Scenario Outline: Mark an order as delivered (Normal flow)
    Given there are certain orders in the system:
      | Customer Email    | Jersey Description | Order Date | Status         |
      | john@gmail.com    | Bulls              | 2025-02-20 | ToBeShipped    |
      | jane@gmail.com    | Lakers             | 2025-02-21 | Shipped        |
      | bob@gmail.com     | Warriors           | 2025-02-22 | Delivered      |
    When I mark an order as delivered with "<Customer Email>" and "<Jersey Description>"
    Then the order status is updated to "Delivered":
      | Customer Email    | Jersey Description | Order Date | Status    |
      | <Customer Email>  | <Jersey Description> | <Order Date> | Delivered |
    Examples:
      | Customer Email    | Jersey Description | Order Date | Initial Status | Updated Status |
      | john@gmail.com    | Bulls              | 2025-02-20 | ToBeShipped    | Delivered      |
      | jane@gmail.com    | Lakers             | 2025-02-21 | Shipped        | Delivered      |
      | bob@gmail.com     | Warriors           | 2025-02-22 | Delivered      | Delivered      |