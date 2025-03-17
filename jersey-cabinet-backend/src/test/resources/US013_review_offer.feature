Feature: Review Offer

As a Customer, 
I want to be able to accept or reject an offer that has been put down for my jersey after its authentification
So that the jersey can be successfully listed.

Background:
    Given the following jerseys exist in the system
        | requestState | description       | brand   | sport      | color | jerseyImage | proofOfAuthenticationImage  | Customer        |
        | unlisted     | This is a jersey. | Nike    | soccer     | black | null        | null                        | "tee@gmail.com" |
        | unlisted     | This is a jersey. | Adidas  | soccer     | black | null        | null                        | "tee@gmail.com" |
        | listed       | This is a jersey. | Nike    | soccer     | black | null        | null                        | "tee@gmail.com" |
    And the following offers exist in the system
        | price | offerState | jerseyId | employeeId |
        | 100   | pending    | 20       | 34         |
        | 150   | pending    | 21       | 34         |
        | 100   | pending    | 22       | 34         |
    
    And I am logged in as a Customer

Scenario Outline: US013: Successfully accept an offer for my authenticated jersey (Normal Flow)
    When I confirm and accept the offer
    Then the offer state becomes accepted
    And the jersey state becomes listed

Scenario Outline: US013: Successfully reject an offer for my authenticated jersey (Alternate Flow)
    When I confirm and reject the offer
    Then the offer state becomes rejected
    And the jersey state remains unlisted

Scenario Outline: US013: Unsuccessfully accept an offer for my authenticated jersey that has already been accepted (Error Flow)
    When I attempt to accept the offer
    Then the system warns the user that an offer has been previously accepted for this jersey
    And the offer state remains unchanged
    And the jersey state remains unchanged