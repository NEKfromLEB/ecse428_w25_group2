Feature: Review Offer

As a Customer, 
I want to be able to accept or reject an offer that has been put down for my jersey after its authentification
So that the jersey can be successfully listed.

Background:
    Given the following offer exists in the system
        | price | offerState | jerseyId | employeeId |
        | 100   | pending    | 20       | 34         |

    And the following jersey exists in the system
        | requestState | description       | brand | sport      | color | jerseyImage | proofOfAuthenticationImage  | Customer        |
        | unlisted     | This is a jersey. | Nike  | soccer     | black | null        | null                        | "tee@gmail.com" |
    
    And I am logged in as a Customer

Scenario Outline: Successfully accept an offer for my authenticated jersey (Normal Flow)
    When I confirm and accept the offer
    Then the offer state becomes accepted
    And the jersey state becomes listed

Scenario Outline: Successfully reject an offer for my authenticated jersey (Alternate Flow)
    When I confirm and reject the offer
    Then the offer state becomes rejected
    And the jersey state remains unlisted

Scenario Outline: Unsuccessfully accept an offer for my authenticated jersey that has already been accepted (Error Flow)
    Given the following offer exists in the system
        | price | offerState | jerseyId | employeeId |
        | 100   | pending    | 20       | 34         |
     And the following jersey exists in the system
        | requestState | description       | brand | sport      | color | jerseyImage | proofOfAuthenticationImage  | Customer        |
        | listed       | This is a jersey. | Nike  | soccer     | black | null        | null                        | "tee@gmail.com" |
    When I attempt to accept the offer
    Then the system displays an error message "An offer has been previously accepted for this jersey."
    And the offer state remains unchanged
    And the jersey state remains unchanged