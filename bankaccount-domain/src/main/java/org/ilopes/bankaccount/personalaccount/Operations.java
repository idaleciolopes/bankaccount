package org.ilopes.bankaccount.personalaccount;

import org.ilopes.bankaccount.ddd.DDD;

import java.util.Collection;

/**
 * This interface defines the contract the repository for Operations entities must implement.
 */
@DDD.Repository
public interface Operations {
    Collection<Operation<?>> findByAccountNumber(AccountNumber accountNumber);
}
