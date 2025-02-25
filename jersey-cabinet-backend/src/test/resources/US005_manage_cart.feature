Feature: Manage the cart
  As a User,
  I want to be able to add items to my cart, remove items from my cart,
  and view the contents of my cart, so that I can purchase them later.

  Background:
    Given the following listings exist in the system
        | id | title                     | price | condition     | sport      |
        | 1  | 2002 Brazil Home          | 150   | Brand new     | soccer     |
        | 2  | 2006 AC Milan Home        | 90    | Used          | soccer     |
        | 3  | 1992 Chicago Bulls        | 200   | Like new      | basketball |
        | 4  | 1984 Las Vegas Raiders    | 80    | Used          | football   |

    And I am logged in as a user

  ########################################################################
  # ADDING ITEMS
  ########################################################################
  Scenario: Successfully add an item to an empty cart
    Given my cart is empty
    And an item "2002 Brazil Home" is available in stock
    When I add "2002 Brazil Home" to my cart
    Then "2002 Brazil Home" should be in my cart
    And the cart should contain 1 item total

  Scenario: Add a second distinct item to a cart
    Given my cart already contains "2002 Brazil Home"
    And an item "2006 AC Milan Home" is available in stock
    When I add "2006 AC Milan Home " to my cart
    Then the cart should contain "2002 Brazil Home" and "2006 AC Milan Home "
    And the cart should contain 2 items total

  Scenario: Attempt to add an item that is already in the cart
    Given my cart already contains "2002 Brazil Home"
    When I add "2002 Brazil Home" to my cart again
    Then I should see an error message "Item already in cart"
    And the cart should still contain "2002 Brazil Home" exactly once
    And the cart should contain 1 item total

  Scenario: Attempt to add an out-of-stock item
    Given an item "2016 Golden State Warriors" is marked out of stock
    When I add "2016 Golden State Warriors" to my cart
    Then I should see an error message "Item is not for sale"
    And the cart should remain unchanged

  Scenario: Attempt to add an item while not logged in
    Given I am not logged in
    And my cart is empty
    And an item "1992 Chicago Bulls" is available in stock
    When I add "1992 Chicago Bulls" to my cart
    Then I should see an error message "You must be logged in to add items"
    And the cart should remain empty

  ########################################################################
  # REMOVING ITEMS
  ########################################################################
  Scenario: Successfully remove an item from the cart
    Given my cart contains "2002 Brazil Home" and "2006 AC Milan Home"
    When I remove "2002 Brazil Home" from the cart
    Then "2002 Brazil Home" should no longer be in my cart
    And the cart should contain "2006 AC Milan Home" only
    And the cart should contain 1 item total

  Scenario: Remove the last item from the cart, making it empty
    Given my cart contains "2006 AC Milan Home" only
    When I remove "2006 AC Milan Home" from the cart
    Then my cart should be empty

  Scenario: Attempt to remove an item that is not in the cart
    Given my cart contains "2006 AC Milan Home" only
    When I remove "1984 Las Vegas Raiders" from the cart
    Then I should see an error message "Item not found in cart"
    And the cart should still contain "2006 AC Milan Home" only

  Scenario: Attempt to remove an item from the cart while not logged in
    Given I am not logged in
    And my cart contains "2006 AC Milan Home"
    When I remove "2006 AC Milan Home" from the cart
    Then I should see an error message "You must be logged in to remove items"
    And the cart should still contain "2006 AC Milan Home"

  ########################################################################
  # VIEWING THE CART
  ########################################################################
  Scenario: View a cart containing multiple items
    Given my cart contains "2006 AC Milan Home" and "1984 Las Vegas Raiders"
    When I view my cart
    Then I should see the items "2006 AC Milan Home" and "1984 Las Vegas Raiders"
    And the cart item count should be 2

  Scenario: View an empty cart
    Given my cart is empty
    When I view my cart
    Then I should see a message "Your cart is empty"
    And the cart item count should be 0

  Scenario: View a cart while not logged in
    Given I am not logged in
    When I attempt to view my cart
    Then I should be prompted to log in
    And I should not be able to see cart details

  ########################################################################
  # CLEARING THE CART
  ########################################################################
  Scenario: Clear the cart containing multiple items
    Given my cart contains "2006 AC Milan Home", "2002 Brazil Home", and "1992 Chicago Bulls"
    When I choose to clear my cart
    Then my cart should be empty
    And I should see a message "Cart cleared successfully"
