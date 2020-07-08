@TestWithJpa @TestWithRest
Feature: Check My Operations

  Background:
    Given the following accounts exists on system :
      | accountNumber                        | lastOperationDate | balance |
      | f86fd526-fd41-4760-b57f-22c5d3bb2469 | today-3           |     150 |
      | 33b75c54-a0e1-4fe4-b196-be9560626e36 | today-10          |      12 |
      | 2565cfa2-1a3b-4d21-b34c-9a410f36d436 | today             |     200 |
    And the following operations were registered :
      |type      | transactionNumber                    | accountNumber                        | dateTime | amount |
      | withdraw | 27b5a629-b3db-41ac-8168-90b83074c7ae | f86fd526-fd41-4760-b57f-22c5d3bb2469 | today-3  |    150 |
      | withdraw | 2b222d89-3df9-4701-8bdd-6eb73fb76c3f | 33b75c54-a0e1-4fe4-b196-be9560626e36 | today-10 |     12 |
      | deposit  | 365d8a23-66d9-4cbd-9846-a225a4a60be0 | 2565cfa2-1a3b-4d21-b34c-9a410f36d436 | today-2  |    125 |
      | withdraw | 76aff881-dfb2-4141-a904-52addc2a3754 | 2565cfa2-1a3b-4d21-b34c-9a410f36d436 | today    |     75 |

    Scenario: check my operations on an existing account
      Given I own account "2565cfa2-1a3b-4d21-b34c-9a410f36d436"
      When I check my operations
      Then I should get operations :
        | type     | transactionNumber                    | accountNumber                        | dateTime | amount | effectiveAmount | creditedAmount | debitedAmount |
        | deposit  | 365d8a23-66d9-4cbd-9846-a225a4a60be0 | 2565cfa2-1a3b-4d21-b34c-9a410f36d436 | today-2  |    125 |             125 |            125 |             0 |
        | withdraw | 76aff881-dfb2-4141-a904-52addc2a3754 | 2565cfa2-1a3b-4d21-b34c-9a410f36d436 | today    |     75 |             -75 |              0 |            75 |

  Scenario: check my operations on an unexisting account
    Given I own account "786b6a6f-67f2-49d3-939c-31244c441bd3"
    When I check my operations
    Then operation should be refused
