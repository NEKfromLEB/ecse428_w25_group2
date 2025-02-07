Feature: Review Authentication Request

As an Employee, 
I want to review authentication requests
so that I can confirm or descredit the authenticity of a jersey.

Scenario Outline: Confirm the authenticity request. (Normal flow)
    Given I am signed in as an Employee
    When I confirm the authencity of a jersey
    Then the Seller receives a message notifying them of the decision
    And the jersey is marked as verified. 

Scenario Outline: Reject the authenticity request. (Normal flow)
    Given I am signed in as an Employee
    When reject the authencity of a jersey 
    Then the Seller receives a message notifying them of the decision
    And the jersey is marked as rejected. 

Scenario Outline: I do not make a decision. (Error flow)
    Given I am signed in as an Employee
    When I do not choose a decision
    Then the jersey status remains unchanged
    And remains in the list of requests to be reviewed. 
