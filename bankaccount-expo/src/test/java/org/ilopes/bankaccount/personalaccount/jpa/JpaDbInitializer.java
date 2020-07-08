package org.ilopes.bankaccount.personalaccount.jpa;

import org.ilopes.bankaccount.personalaccount.AccountStatus;
import org.ilopes.bankaccount.personalaccount.DbInitializer;
import org.ilopes.bankaccount.personalaccount.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * DbInitializer for JPA context.
 */
@Component
class JpaDbInitializer implements DbInitializer {
    private AccountStatusesJpaRepository accountStatusesJpaRepository;
    private OperationsJpaRepository operationsJpaRepository;

    private JpaDbInitializer(AccountStatusesJpaRepository accountStatusesJpaRepository,
                             OperationsJpaRepository operationsJpaRepository) {
        this.accountStatusesJpaRepository = accountStatusesJpaRepository;
        this.operationsJpaRepository = operationsJpaRepository;
    }

    public void saveAccountStatus(AccountStatus accountStatus) {
        accountStatusesJpaRepository.save(accountStatus);
    }

    public void saveOperation(Operation<?> operation) {
        operationsJpaRepository.save(operation);
    }
}
