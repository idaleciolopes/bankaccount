package org.ilopes.bankaccount.personalaccount;

import java.util.Optional;
import org.ilopes.bankaccount.ddd.DDD;

/**
 * This interface defines the contract the repository for AccountStatus entities must implement.
 */
@DDD.Repository
public interface AccountStatuses {
    Optional<AccountStatus> findByAccountNumber(AccountNumber accountNumber);
}
