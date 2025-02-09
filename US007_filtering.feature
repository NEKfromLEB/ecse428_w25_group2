Feature: Filter Items on the Jersey Cabinet Website
As a user,
I want to filter through available jerseys
so that I can quickly find and purchase the ones that interest me.

Background:
Given the user is on the Jersey Cabinet homepage
And the user navigates to the "Shop" page

Scenario: Filter by Team
Given the user is on the "Shop" page listing all jerseys
When the user selects "Team" from the filter options
And the user types "Los Angeles Lakers" into the Team filter
Then only jerseys belonging to the "Los Angeles Lakers" team are displayed
And the user can see the count of items filtered by "Los Angeles Lakers"

Scenario: Filter by Price Range
Given the user is on the "Shop" page listing all jerseys
When the user sets the minimum price to "100"
And the user sets the maximum price to "250"
And the user clicks the "Apply Filter" button
Then the user should see only jerseys priced between $100 and $250
And the user should see how many jerseys match this price range

Scenario: Invalid Price Range (Error Flow)
Given the user is on the "Shop" page listing all jerseys
When the user sets the minimum price to "-10"
And the user sets the maximum price to "50"
And the user clicks the "Apply Filter" button
Then the user should see an error message indicating "Invalid price range"
And the user should not see any filtered results

Scenario: Filter by Size
Given the user is on the "Shop" page listing all jerseys
When the user selects "Size" from the filter options
And the user chooses "Large"
Then only jerseys available in size "Large" should be displayed

Scenario: Combining Multiple Filters
Given the user is on the "Shop" page listing all jerseys
When the user selects "Team" = "Chicago Bulls"
And the user selects "Size" = "Medium"
And the user clicks the "Apply Filter" button
Then only jerseys that are "Medium" size for the "Chicago Bulls" should be displayed
And the user should see the filtered count of matching jerseys

Scenario: Clear Filters
Given the user has applied filters for "Team" = "Chicago Bulls" and "Size" = "Medium"
When the user clicks the "Clear Filters" button
Then all filtering criteria are removed
And the user sees the full list of available jerseys again
