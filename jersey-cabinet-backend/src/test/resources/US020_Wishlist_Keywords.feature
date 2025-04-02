Feature: Obtain Matching Jersey List
  As a Customer,  
  I want to obtain the list of jerseys that match the keywords in my wishlist,  
  So that I can know what's on sale.

  Background:
    Given the system has an updated wishlist repository
    And the wishlist service implements the identifyJerseys functionality
    And the sale information is current

  Scenario Outline: Successfully retrieve matching jerseys based on wishlist keywords
    Given the customer is logged in
    And the customer's wishlist contains the keyword "<keyword>"
    When the customer requests to view jerseys on sale matching their wishlist
    Then the system displays a list of jerseys matching "<keyword>"
    And each jersey entry shows its sale price, availability, and sale date
    And the system returns results in relevance order for "<keyword>"

    Examples:
      | keyword     |
      | Nike       |
      | Adidas     |
      | Puma       |
      | Reebok     |

  Scenario Outline: Successfully retrieve jerseys with multiple keywords
    Given the customer is logged in
    And the customer's wishlist contains the keywords "<keywords>"
    When the customer requests to view jerseys on sale matching their wishlist
    Then the system displays jerseys that match any of the keywords in "<keywords>"
    And the system handles duplicate keywords correctly

    Examples:
      | keywords           |
      | Nike, Basketball   |
      | Adidas, Soccer    |

  Scenario Outline: Search by specific attributes
    Given the customer is logged in
    And the customer's wishlist contains the keyword "<attribute_value>"
    When the customer requests to view jerseys on sale matching their wishlist
    Then the system finds jerseys with <attribute_type> "<attribute_value>"

    Examples:
      | attribute_type | attribute_value |
      | color         | Red             |
      | sport         | Basketball      |
      | color         | Blue            |
      | sport         | Soccer          |

  Scenario Outline: Handle special characters and case variations
    Given the customer is logged in
    And the customer's wishlist contains special characters "<keyword>"
    When the customer requests to view jerseys on sale matching their wishlist
    Then the system handles special characters in the results

    Examples:
      | keyword           |
      | Nike*Star         |
      | Nike & Adidas     |

  Scenario Outline: Case insensitive matching
    Given the customer is logged in
    And the customer's wishlist contains case variations "<keyword>"
    When the customer requests to view jerseys on sale matching their wishlist
    Then the system displays a list of jerseys matching "<expected>"

    Examples:
      | keyword    | expected  |
      | NIKE      | Nike      |
      | adidas    | Adidas    |

  Scenario Outline: No matching jerseys found
    Given the customer is logged in
    And the customer's wishlist contains the keyword "<keyword>"
    When the customer requests to view jerseys on sale matching their wishlist
    Then the system displays a message "No matching jerseys found."

    Examples:
      | keyword      |
      | Nonexistent  |
      | Unknown      |

  Scenario: Empty wishlist
    Given the customer is logged in
    And the customer's wishlist is empty or has no defined keywords "(none)"
    When the customer requests to view jerseys on sale matching their wishlist
    Then the system displays a message "Please add keywords to your wishlist to retrieve matching jerseys."
