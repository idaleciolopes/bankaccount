package org.ilopes.bankaccount.personalaccount;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Dummy implementation of a transaction number provider based on a random creation of class 4 UUID. On real life
 * scenarios this should be more complex with cached pre-allocated numbers and unicity check.
 */
@Service
public class DummyTransactionNumberProvider implements TransactionNumberProvider {
    @Override
    public TransactionNumber giveTransactionNumberForNewTransaction() {
        return new TransactionNumber(UUID.randomUUID());
    }
}
