Feature: Manage the cart
  As a User,
  I want to be able to add items to my cart, remove items from my cart,
  and view the contents of my cart, so that I can purchase them later.

  Background:
    Given the following listings exist in the system
        | id | title                     | price | color         | sport      |
        | 1  | 2002 Brazil Home          | 150   | Green         | soccer     |
        | 2  | 2006 AC Milan Home        | 90    | Red           | soccer     |
        | 3  | 1992 Chicago Bulls        | 200   | Red           | basketball |
        | 4  | 1984 Las Vegas Raiders    | 80    | Black         | football   |


  ########################################################################
  # ADDING ITEMS
  ########################################################################
  Scenario: US005: Successfully add an item to an empty cart
    Given my cart is empty
    And an item "2002 Brazil Home" is available in stock
    When I add "2002 Brazil Home" to my cart
    Then "2002 Brazil Home" should be in my cart
    And the cart should contain 1 item total

  Scenario: US005: Add a second distinct item to a cart
    Given my cart contains "2002 Brazil Home"
    And an item "2006 AC Milan Home" is available in stock
    When I add "2006 AC Milan Home" to my cart
    Then the cart should contain "2002 Brazil Home" and "2006 AC Milan Home"
    And the cart should contain 2 item total

  Scenario: US005: Attempt to add an item that is already in the cart
    Given my cart contains "2002 Brazil Home"
    When I add "2002 Brazil Home" to my cart
    Then I should see an error message "Item already in cart"
    Then "2002 Brazil Home" should be in my cart
    And the cart should contain 1 item total

  Scenario: US005: Attempt to add an out-of-stock item
    Given an item "1992 Chicago Bulls" is marked out of stock
    When I add "1992 Chicago Bulls" to my cart
    Then I should see an error message "Item is not for sale"
    And the cart should contain 0 item total


  ########################################################################
  # REMOVING ITEMS
  ########################################################################
  Scenario: US005: Successfully remove an item from the cart
    Given my cart contains "2002 Brazil Home" and "2006 AC Milan Home"
    When I remove "2002 Brazil Home" from the cart
    Then "2002 Brazil Home" should no longer be in my cart
    Then "2006 AC Milan Home" should be in my cart
    And the cart should contain 1 item total

  Scenario: US005: Remove the last item from the cart, making it empty
    Given my cart contains "2006 AC Milan Home"
    When I remove "2006 AC Milan Home" from the cart
    And the cart should contain 0 item total

  Scenario: US005: Attempt to remove an item that is not in the cart
    Given my cart contains "2006 AC Milan Home"
    When I remove "1984 Las Vegas Raiders" from the cart
    Then I should see an error message "Item not found in cart"
    Then "2006 AC Milan Home" should be in my cart


  ########################################################################
  # VIEWING THE CART
  ########################################################################
  Scenario: US005: View a cart containing multiple items
    Given my cart contains "2006 AC Milan Home" and "1984 Las Vegas Raiders"
    When I view my cart
    Then I should see the items "2006 AC Milan Home" and "1984 Las Vegas Raiders"
    And the cart should contain 2 item total

  Scenario: US005: View an empty cart
    Given my cart is empty
    When I view my cart
    Then I should see a message "Your cart is empty"
    And the cart should contain 0 item total


  ########################################################################
  # CLEARING THE CART
  ########################################################################
  Scenario: US005: Clear the cart containing multiple items
    Given my cart contains "2006 AC Milan Home" and "1992 Chicago Bulls"
    When I choose to clear my cart
    And the cart should contain 0 item total
    And I should see a message "Cart cleared successfully"
