package org.ilopes.bankaccount.personalaccount.jpa;

import org.ilopes.bankaccount.personalaccount.AccountNumber;
import org.ilopes.bankaccount.personalaccount.Operation;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
class OperationsJpaBridge implements org.ilopes.bankaccount.personalaccount.Operations {
    private OperationsJpaRepository operationsJpaRepository;

    // java:S1144 this constructor will be used by Spring.
    @SuppressWarnings("java:S1144")
    private OperationsJpaBridge(OperationsJpaRepository operationsJpaRepository) {
        this.operationsJpaRepository = operationsJpaRepository;
    }

    @java.lang.Override
    public Collection<Operation<?>> findByAccountNumber(AccountNumber accountNumber) {
        return operationsJpaRepository.findByAccountNumber(accountNumber);
    }

    @java.lang.Override
    public void registerOperation(Operation<?> operation) {
        operationsJpaRepository.save(operation);
    }
}
