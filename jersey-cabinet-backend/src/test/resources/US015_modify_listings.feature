Feature: Modify/Delete Listings

As an Employee, 
I want to be able to modify jerseys listings, including deleting them
so that I can rectify any errors and keep the information up to date.

Background:
    Given I am logged in as an Employee
    And the following listed jersey is selected 
        | requestState| description       | brand | sport      | color | jerseyImage | proofOfAuthenticationImage  | SalePrice | Customer
        | Listed      | This is a jersey. | Nike  | soccer     | black | google      | www.drive.com             |    65     | "tee@gmail.com"

Scenario Outline: US015-Successfully modify a listing (Normal Flow)
    When I modify a "<field>" with "<new_information>"
    Then the listing is updated to reflect the change 

    Examples:
    | field                      | new_information  | 
    | description                | Is this a jersey?|
    | brand                      | Adidas           |
    | sport                      | Basketball       | 
    | color                      | Orange           |
    | jerseyImage                | pixiv            | 
    | proofOfAuthenticationImage | google           |
    | salePrice                  | 100              |




Scenario: US015-Successfully delete a jersey (Normal Flow)
    When I request to delete the jersey I've selected
    Then the jersey is deleted

Scenario: US015-Unsuccessfully try to delete a jersey that is not listed (Error Flow)
    Given the jersey has been bought
    When I try to delete it
    Then the system warns me that I cannot
    And no changes are made

Scenario: US015-Unsuccessfully try to modify a jersey that is not listed (Error Flow)
    Given the jersey has been bought
    When I try to modify its description to "jerseying"
    Then the system warns me that I cannot
    And no changes are made 


