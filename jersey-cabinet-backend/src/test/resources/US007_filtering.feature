Feature: Filter Jersey Listings
  As a user of the Jersey Cabinet application,
  I want to filter jersey listings based on sport and description,
  So that I can quickly narrow down the results to the jerseys I’m interested in.

  Background:
    Given the following jersey listings exist in the system
      | id | description             | sport      |
      | 1  | 2002 Brazil Home        | soccer     |
      | 2  | 2006 AC Milan Home      | soccer     |
      | 3  | 1992 Chicago Bulls      | basketball |
      | 4  | 1984 Las Vegas Raiders  | football   |
    And I am logged in as a user

  Scenario: Filter listings by sport and description substring
    When the user filters the listings with sport "soccer" and description containing "Home"
    Then the system returns the following jersey listings: "2002 Brazil Home, 2006 AC Milan Home"

  Scenario: Filter listings by sport only
    When the user filters the listings with sport "basketball"
    Then the system returns the following jersey listings: "1992 Chicago Bulls"

  Scenario: Filter listings by description substring only
    When the user filters the listings with description containing "Raiders"
    Then the system returns the following jersey listings: "1984 Las Vegas Raiders"

  Scenario: No matching listings
    When the user filters the listings with sport "soccer" and description containing "Bulls"
    Then the system returns the following jersey listings: ""
    And a warning message is shown that 0 matches were found
