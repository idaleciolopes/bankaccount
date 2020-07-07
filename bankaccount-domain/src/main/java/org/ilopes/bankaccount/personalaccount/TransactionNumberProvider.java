package org.ilopes.bankaccount.personalaccount;

import org.ilopes.bankaccount.ddd.DDD;

/**
 * This domain service creates an account number
 */
@DDD.DomainService
public interface TransactionNumberProvider {
    /**
     * Gives a number for a new transaction.
     */
    TransactionNumber giveTransactionNumberForNewTransaction();
}
