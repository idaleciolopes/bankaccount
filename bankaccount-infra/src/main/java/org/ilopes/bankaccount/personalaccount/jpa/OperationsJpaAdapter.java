package org.ilopes.bankaccount.personalaccount.jpa;

import org.ilopes.bankaccount.personalaccount.AccountNumber;
import org.ilopes.bankaccount.personalaccount.Operation;
import org.ilopes.bankaccount.personalaccount.TransactionNumber;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
class OperationsJpaAdapter implements org.ilopes.bankaccount.personalaccount.Operations {
    private OperationsJpaRepository operationsJpaRepository;

    // java:S1144 this constructor will be used by Spring.
    @SuppressWarnings("java:S1144")
    private OperationsJpaAdapter(OperationsJpaRepository operationsJpaRepository) {
        this.operationsJpaRepository = operationsJpaRepository;
    }

    @Override
    public Optional<Operation<?>> findByTransactionNumber(TransactionNumber transactionNumber) {
        return operationsJpaRepository.findByTransactionNumber(transactionNumber);
    }

    @Override
    public Collection<Operation<?>> findByAccountNumber(AccountNumber accountNumber) {
        return operationsJpaRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public void registerOperation(Operation<?> operation) {
        operationsJpaRepository.save(operation);
    }
}
