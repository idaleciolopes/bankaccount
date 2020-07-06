package org.ilopes.bankaccount.personnalaccount;

import org.ilopes.bankaccount.personalaccount.AccountNumber;
import org.ilopes.bankaccount.personalaccount.AccountNumberProvider;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Dummy implementation of an account number provider based on a random creation of class 4 UUID. On real life scenarios
 * this should be more complex with cached pre-allocated numbers and unicity check.
 */
@Service
public class DummyAccountNumberProvider implements AccountNumberProvider {
    @Override
    public AccountNumber giveAccountNumberForNewAccount() {
        return new AccountNumber(UUID.randomUUID());
    }
}
