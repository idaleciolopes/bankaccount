Feature: Deposit money on my account

  Background:
    Given the following accounts exists on system :
      | accountNumber                        | lastOperationDate | balance |
      | caefc76d-0448-4b8c-9899-9b7cd4961564 | today-3           |     150 |
      | 1658c96c-097e-43a8-a72d-d18958fb3071 | today-10          |      12 |
      | 48966b87-23a0-4249-8845-5f80425c3d43 | today             |     200 |
    And the following operations were registered :
      | transactionNumber                    | accountNumber                        | dateTime | amount |
      | e4abc229-4a03-4436-9952-ab4c28da7bd5 | caefc76d-0448-4b8c-9899-9b7cd4961564 | today-3  |    150 |
      | 17e4cc6e-8834-4bec-b56e-83ed597746a0 | 1658c96c-097e-43a8-a72d-d18958fb3071 | today-10 |     12 |
      | 4e17c9ce-4d7e-489c-956d-e02d5c4e5be2 | 48966b87-23a0-4249-8845-5f80425c3d43 | today-2  |    125 |
      | 82fcafd1-4c96-4c77-b898-252734e85083 | 48966b87-23a0-4249-8845-5f80425c3d43 | today    |     75 |


  # Regular scenario a user deposit some money on existing account
  Scenario:
    Given I own account "caefc76d-0448-4b8c-9899-9b7cd4961564"
    When I deposit 24 on it
    Then I should have a balance of 174
    And my account has now 2 operation(s)

  # A user deposits some money on an unexisting account
  Scenario:
    Given I own account "50dc0470-e8c2-4cb4-8113-661aaf193349"
    When I deposit 14 on it
    Then operation should be refused
    And account "50dc0470-e8c2-4cb4-8113-661aaf193349" doesn't exist

  # A user deposits a negative amount of money on an existing account
  Scenario:
    Given I own account "caefc76d-0448-4b8c-9899-9b7cd4961564"
    When I deposit -56 on it
    Then operation should be invalid
    And I should have a balance of 150
    And my account still has 1 operation(s)