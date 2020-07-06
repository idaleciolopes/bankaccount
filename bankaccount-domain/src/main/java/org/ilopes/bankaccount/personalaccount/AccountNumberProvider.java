package org.ilopes.bankaccount.personalaccount;

import org.ilopes.bankaccount.ddd.DDD;

/**
 * This domain service creates an account number
 */
@DDD.DomainService
public interface AccountNumberProvider {
    /**
     * Gives a number for a new account.
     */
    AccountNumber giveAccountNumberForNewAccount();
}
