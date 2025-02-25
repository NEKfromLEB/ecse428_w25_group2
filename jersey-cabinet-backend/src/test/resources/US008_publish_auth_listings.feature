Feature: Listing a Jersey for Sale
  As a User,
  I want to be able to list my jersey for sale,
  So that other Users can view the product and purchase it.

  Background:
    Given I am a logged-in User

  Scenario Outline: Successfully listing a jersey for sale (Normal Flow)
    When I navigate to the "Sell Jersey" page
    And I provide the required details including "<brand>", "<size>", "<condition>", and <price>
    And I upload a clear image of the jersey
    And I click on the "List Jersey" button
    Then my jersey should be successfully listed for sale
    And other Users should be able to view it in the marketplace

  Examples:
    | brand      | size  | condition | price |
    | Nike      | M     | New       | 120   |
    | Adidas    | L     | Used      | 80    |

  Scenario Outline: Attempting to list a jersey with missing required details (Error Flow)
    When I navigate to the "Sell Jersey" page
    And I leave the "<missing_field>" field empty
    And I click on the "List Jersey" button
    Then I should see an error message "<error_message>"
    And my jersey should not be listed for sale

  Examples:
    | missing_field | error_message       |
    | price        | Price is required   |
    | brand        | Brand is required   |

  Scenario Outline: Editing an existing jersey listing (Alternate Flow)
    Given I have already listed a jersey for sale
    And I am on the "My Listings" page
    When I click on the "Edit" button for my jersey
    And I update the "<updated_field>" to "<new_value>"
    And I click on "Save Changes"
    Then my jersey listing should be updated successfully
    And the updated details should be visible to potential buyers

  Examples:
    | updated_field | new_value |
    | price        | 100       |
    | condition    | Like new  |

