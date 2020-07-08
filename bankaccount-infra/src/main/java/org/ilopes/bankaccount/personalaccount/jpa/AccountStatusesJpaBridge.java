package org.ilopes.bankaccount.personalaccount.jpa;

import org.ilopes.bankaccount.personalaccount.AccountNumber;
import org.ilopes.bankaccount.personalaccount.AccountStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * This class implements a repository for account status datas on JPA context. In fact this class is just a bridge to the
 * real JPA repository.
 */
@Component
class AccountStatusesJpaBridge implements org.ilopes.bankaccount.personalaccount.AccountStatuses {
    private AccountStatusesJpaRepository jpaAccountStatuses;

    // java:S1144 This constructor will be used by Spring
    @SuppressWarnings("java:S1144")
    private AccountStatusesJpaBridge(AccountStatusesJpaRepository jpaAccountStatuses) {
        this.jpaAccountStatuses = jpaAccountStatuses;
    }

    @Override
    public Optional<AccountStatus> findByAccountNumber(AccountNumber accountNumber) {
        return jpaAccountStatuses.findById(accountNumber);
    }
}
