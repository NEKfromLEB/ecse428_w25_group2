Feature: User Authentication

  Scenario Outline: US006: User successfully creates an account
    Given the user is on the registration page
    When the user enters a valid "<email>" and "<password>"
    And clicks the Sign Up button
    Then the account should be created successfully
    And the user should see a confirmation message

    Examples:
      | email                | password     |
      | user@example.com     | Passw0rd!    |
      | testuser@email.com   | Secure123$   |

  Scenario Outline: US006: User fails to create an account due to invalid credentials
    Given the user is on the registration page
    When the user enters an invalid "<email>" or "<password>"
    And clicks the Sign Up button
    Then the system should display an appropriate error message

    Examples:
      | email            | password |
      | invalidemail.com | Pass123  |
      | user@domain.com  | short    |

  Scenario Outline: US006: User successfully logs in
    Given the user has an existing account with "<email>" and "<password>"
    And is on the login page
    When the user enters a valid "<email>" and "<password>"
    And clicks the Login button
    Then the user should be redirected to the homepage

    Examples:
      | email                | password   |
      | user@example.com     | Passw0rd!  |
      | testuser@email.com   | Secure123$ |

  Scenario Outline: US006: User fails to log in with incorrect credentials
    Given the user is on the login page
    When the user enters an incorrect "<email>" or "<password>"
    And clicks the Login button
    Then the system should display an "Invalid credentials" message

    Examples:
      | email                | password    |
      | user@example.com     | WrongPass   |
      | unknown@email.com    | Passw0rd!   |

  Scenario: US006: User tries to log in with an unregistered email
    Given the user is on the login page
    When the user enters an unregistered email newuser@example.com
    And clicks the Login button
    Then the system should display a User not found message


