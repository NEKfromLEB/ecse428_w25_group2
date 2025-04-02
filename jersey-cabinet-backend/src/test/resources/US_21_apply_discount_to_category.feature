Feature: Apply category-wide discount on jerseys

  As an Employee,
  I want to apply a category-wide discount on jerseys,
  So that customers can benefit from reduced prices.

  Scenario: Employee applies a 20% discount to all jerseys in the "Soccer" category (Normal Flow)
    Given the following jerseys exist:
      | sport      | salePrice |
      | Soccer     | 100       |
      | Soccer     | 150       |
      | Basketball | 200       |
    And I am logged in as an Employee
    When I apply a 20% discount to all jerseys in the "Soccer" category
    Then the sale price of the jerseys in the "Soccer" category should be updated:
      | salePrice |
      | 80        |
      | 120       |
    And the sale price of jerseys in other categories should remain unchanged:
      | sport      | salePrice |
      | Basketball | 200       |

  Scenario:  Employee applies a discount to a category with no jerseys (Alternate Flow)
    Given the following jerseys exist:
      | sport      | salePrice |
      | Basketball | 300       |
      | Basketball | 400       |
    And I am logged in as an Employee
    When I apply a 30% discount to all jerseys in the "Soccer" category
    Then no jerseys should be updated
    And an error message "No jerseys found in the specified category" should be displayed

  Scenario:  Invalid discount percentage (Error Flow)
    Given the following jerseys exist:
      | sport      | salePrice |
      | Soccer     | 500       |
      | Soccer     | 600       |
    And I am logged in as an Employee
    When I attempt to apply a -10% discount to all jerseys in the "Soccer" category
    Then no jerseys should be updated
    And an error message "Invalid discount percentage: Discount must be between 0% and 100%" should be displayed