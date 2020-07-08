package org.ilopes.bankaccount.personalaccount;

import org.ilopes.bankaccount.ddd.DDD;

import java.util.Set;
import java.util.Optional;

/**
 * This interface defines the contract the repository for Operations entities must implement.
 */
@DDD.Repository
public interface Operations {
    /**
     * Gets an operation from its transaction number.
     */
    Optional<Operation<?>> findByTransactionNumber(TransactionNumber transactionNumber);

    /**
     * Finds all the operations linked to an account.
     * @param accountNumber the number of the account to seek.
     * @return the operations linked to the account.
     */
    Set<Operation<?>> findByAccountNumber(AccountNumber accountNumber);

    /**
     * Register an operation in the repository.
     *
     * @param operation the operation to register.
     */
    void registerOperation(Operation<?> operation);
}
