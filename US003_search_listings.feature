Feature: Search for Listings
As a user, I want to be able to search for specific jersey listings so that I can complete my purchase quickly.

Background:
    Given the following listings exist in the system
        | id | title                     | price | condition     | sport      |
        | 1  | 2002 Brazil Home          | 150   | Brand new     | soccer     |
        | 2  | 2006 AC Milan Home        | 90    | Used          | soccer     |
        | 3  | 1992 Chicago Bulls        | 200   | Like new      | basketball |
        | 4  | 1984 Las Vegas Raiders    | 80    | Used          | football   |

    And I am logged in as a user

Scenario Outline: Successfully search for listings through the title (Normal Flow)
    When the user searches for "<search_query>"
    Then a list of "<listings>" shall be displayed where each listing's title contains the search query as a substring

    Examples:
        | search_query | listings                             |
        | Chicago      | 1992 Chicago Bulls                   |
        | Home         | 2002 Brazil Home, 2006 AC Milan Home |

Scenario Outline: Successfully search for listings within a range of prices (Alternate Flow)
    When the user searches for listings between "<min_price>" and "<max_price>" 
    Then a list of "<listings>" shall be displayed where each title's price is in between the "<min_price>" and "<max_price>" inclusively

    Examples:
        | min_price | max_price | listings                                   |
        | 0         | 100       | 2006 AC Milan Home, 1984 Las Vegas Raiders |
        | 100       | 150       | 2002 Brazil Home                           |

Scenario Outline: Successfully search for listings of a specific condition (Alternate Flow)
    When the user searches for listings indicating a "<condition>"
    Then a list of "<listings>" that have "<condition>" shall be displayed 

    Examples:
        | condition | listings                                   |
        | Like new  | 1992 Chicago Bulls                         |
        | Used      | 2006 AC Milan Home, 1984 Las Vegas Raiders |

Scenario Outline: Successfully search for listings of a specific sport (Alternate Flow)
    When the user searches for listings indicating a "<sport>"
    Then a list of "<listings>" of "<sport>" shall be displayed

    Examples:
        | sport    | listings                             |
        | soccer   | 2002 Brazil Home, 2006 AC Milan Home |
        | football | 1984 Las Vegas Raiders               |

Scenario Outline: Unsuccessfully search for listings because query does not match any listing (Error Flow)
    Given no listings match "<search_query>"
    When the user searches for "<search_query>"
    Then "<listings>" is empty
    And the system warns the user that 0 matches were found

    Examples:
        | search_query | listings |
        | Liverpool    |          |
