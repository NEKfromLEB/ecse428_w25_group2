Feature: Apply category-wide discount on jerseys

  As an Employee,
  I want to apply a category-wide discount on jerseys,
  So that customers can benefit from reduced prices.

  Scenario: Employee applies a 20% discount to all jerseys in the "Soccer" category (Normal Flow)
    Given the following jerseys exist:
      | id | sport  | salePrice |
      | 1  | Soccer | 100.00    |
      | 2  | Soccer | 150.00    |
      | 3  | Basketball | 200.00 |
    And I am logged in as an Employee
    When I apply a 20% discount to all jerseys in the "Soccer" category
    Then the sale price of the jerseys in the "Soccer" category should be updated:
      | id | salePrice |
      | 1  | 80.00     |
      | 2  | 120.00    |
    And the sale price of jerseys in other categories should remain unchanged:
      | id | salePrice |
      | 3  | 200.00    |

  Scenario:  Employee applies a discount to a category with no jerseys (Alternate Flow)
    Given the following jerseys exist:
      | id | sport  | salePrice |
      | 4  | Basketball | 300.00 |
      | 5  | Basketball | 400.00 |
    And I am logged in as an Employee
    When I apply a 30% discount to all jerseys in the "Soccer" category
    Then no jerseys should be updated
    And an error message "No jerseys found in the specified category" should be displayed

  Scenario:  Invalid discount percentage (Error Flow)
    Given the following jerseys exist:
      | id | sport  | salePrice |
      | 6  | Soccer | 500.00    |
      | 7  | Soccer | 600.00    |
    And I am logged in as an Employee
    When I attempt to apply a -10% discount to all jerseys in the "Soccer" category
    Then no jerseys should be updated
    And an error message "Invalid discount percentage: Discount must be between 0% and 100%" should be displayed

  Scenario: Jersey sale price becomes negative after applying discount (Error Flow)
    Given the following jerseys exist:
      | id | sport  | salePrice |
      | 8  | Soccer | 10.00     |
      | 9  | Soccer | 20.00     |
    And I am logged in as an Employee
    When I attempt to apply a 90% discount to all jerseys in the "Soccer" category
    Then no jerseys should be updated
    And an error message "Invalid operation: Sale price cannot be negative" should be displayed