@TestWithMocks @TestWithJpa @TestWithRest
Feature: Withdrawal of money from my account

  Background:
    Given the following accounts exists on system :
      | accountNumber                        | lastOperationDate | balance |
      | a7f93eb4-5925-4833-9b9e-92d691ee707c | today-3           |     150 |
      | 25a03ca7-395d-40dd-ae38-da77f127c0e5 | today-10          |      12 |
      | 59f6c5fa-3b77-4001-a0dc-c2f9d7ce6ec4 | today             |     200 |
    And the following operations were registered :
      | type     | transactionNumber                    | accountNumber                        | dateTime | amount |
      | withdraw | c84c908a-d6f6-4f2d-8691-d7bf4213648c | a7f93eb4-5925-4833-9b9e-92d691ee707c | today-3  |    150 |
      | withdraw | 2d9bcb84-0177-4d30-8ffe-668f994a9ec2 | 25a03ca7-395d-40dd-ae38-da77f127c0e5 | today-10 |     12 |
      | withdraw | b8291a2c-d51b-46f7-b098-68ee66fed903 | 59f6c5fa-3b77-4001-a0dc-c2f9d7ce6ec4 | today-2  |    125 |
      | withdraw | 1cfc0256-a3cf-4ff8-9486-68d296e01fef | 59f6c5fa-3b77-4001-a0dc-c2f9d7ce6ec4 | today    |     75 |


  Scenario: a user withdraw some money from an existing account
    Given I own account "a7f93eb4-5925-4833-9b9e-92d691ee707c"
    When I withdraw 24 from it
    Then I should have a balance of 126
    And my account has now 2 operation(s)


  Scenario: a user withdraw an amount of money from an existing account greater than the account balance
    Given I own account "59f6c5fa-3b77-4001-a0dc-c2f9d7ce6ec4"
    When I withdraw 400 from it
    Then operation should be refused
    And I should have a balance of 200
    And my account still has 2 operation(s)

  Scenario: a user withdraw some money from an unexisting account
    Given I own account "50dc0470-e8c2-4cb4-8113-661aaf193349"
    When I withdraw 14 from it
    Then operation should be refused
    And account "50dc0470-e8c2-4cb4-8113-661aaf193349" doesn't exist

  Scenario: a user withdraw a negative amount of money from an existing account
    Given I own account "59f6c5fa-3b77-4001-a0dc-c2f9d7ce6ec4"
    When I withdraw -56 from it
    Then operation should be invalid
    And I should have a balance of 200
    And my account still has 2 operation(s)

