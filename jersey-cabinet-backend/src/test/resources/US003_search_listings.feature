Feature: Search for Listings
As a user, I want to be able to search for specific jersey listings so that I can complete my purchase quickly.

Background:
    Given the following listings exist in the system
        | id | description               | sport      |
        | 1  | 2002 Brazil Home          | soccer     |
        | 2  | 2006 AC Milan Home        | soccer     |
        | 3  | 1992 Chicago Bulls        | basketball |
        | 4  | 1984 Las Vegas Raiders    | football   |

    And I am logged in as a user

Scenario Outline: US003: Successfully search for listings through the description (Normal Flow)
    When the user searches for "<search_query>"
    Then a list of "<listings>" shall be displayed where each listing's description contains the search query as a substring

    Examples:
        | search_query | listings                             |
        | Chicago      | 1992 Chicago Bulls                   |
        | Home         | 2002 Brazil Home, 2006 AC Milan Home |

Scenario Outline: US003: Successfully search for listings of a specific sport (Alternate Flow)
    When the user searches for listings indicating a "<sport>"
    Then a list of "<listings>" of "<sport>" shall be displayed

    Examples:
        | sport    | listings                             |
        | football | 1984 Las Vegas Raiders               |

Scenario Outline: US003: Unsuccessfully search for listings because query does not match any listing (Error Flow)
    Given no listings match "<search_query>"
    When the user searches for "<search_query>"
    Then "<listings>" is empty
    And the system warns the user that 0 matches were found

    Examples:
        | search_query | listings |
        | Liverpool    |          |
