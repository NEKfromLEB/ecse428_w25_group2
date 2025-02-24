Feature: Review Authentication Request

As an Employee, 
I want to review authentication requests
so that I can confirm or descredit the authenticity of a jersey.

Background:
    Given the following authentication request exists in the system
        | requestState| description       | brand | sport      | color | jerseyImage | proofOfAuthenticationImage  | Customer
        | null        | This is a jersey. | Nike  | soccer     | black | null        | null                        | "tee@gmail.com"
    And I am logged in as an Employee
    And I am viewing the list of pending requests

Scenario Outline: Confirm the authenticity request. (Normal flow)
    When I confirm the authencity of the jersey 
    Then the jersey is marked as unlisted
    And the jersey is assigned to me. 


Scenario Outline: Reject the authenticity request. (Normal flow)
    When I reject the authencity of the jersey 
    Then the jersey is marked as rejected
    And the jersey is assigned to me. 

Scenario Outline: I do not make a decision. (Error flow)
    When I view a request but do not make a decision 
    Then the jersey status remains unchanged
    And the jersey remains in the list of requests to be reviewed.
