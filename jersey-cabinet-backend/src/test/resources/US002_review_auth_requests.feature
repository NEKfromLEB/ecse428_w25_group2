Feature: Review Authentication Request

As an Employee, 
I want to review authentication requests
so that I can confirm or descredit the authenticity of a jersey.

Background:
    Given the following authentication requests exist in the system
        | id |requestState| description       | brand | sport      | color | jerseyImage | proofOfAuthenticationImage | Employee | Customer
        | 2  | null       | This is a jersey. | Nike  | soccer     | black | null        | null                       | null     | "tee@gmail.com"
        | 3  | null       | A jersey this is. | Adidas| basketball | blue  | null        | null                       | null     | "tee@gmail.com"
        | 4  | null       | Is this a jersey? | Puma  | football   | orange| null        | null                       | null     | "tee@gmail.com"

    And I am logged in as an Employee
    And I am viewing the list of pending requests

Scenario Outline: Confirm the authenticity request. (Normal flow)
    When I confirm the authencity of the jersey "<id>"
    And the jersey "<id>" is marked as unlisted
    And the jersey "<id>" is assigned to me. 

Scenario Outline: Reject the authenticity request. (Normal flow)
    When I reject the authencity of the jersey "<id>" 
    And the jersey "<id>" is marked as rejected
    And the jersey "<id>" is assigned to me. 

Scenario Outline: I do not make a decision. (Error flow)
    When I view a request "<id>" but do not make a decision 
    Then the jersey "<id>" status remains unchanged
    And the jersey "<id>" remains in the list of requests to be reviewed.
