Feature: Wishlist Functionality for Jerseys

  As a Customer,
  I want to be able to add jerseys to my wishlist
  So that I can check the wishlist in the future for the jerseys I wanted.

  Background:
    Given the customer is logged into their account

  Scenario: Customer adds a jersey to their wishlist
    When the customer requests to add a jersey to their wishlist
    Then the jersey is added to the customer's wishlist

  Scenario: Customer views their wishlist
    When the customer requests to see their wishlist
    Then the wishlist displays all previously added jerseys