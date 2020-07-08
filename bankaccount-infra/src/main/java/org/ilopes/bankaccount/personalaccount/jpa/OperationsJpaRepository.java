package org.ilopes.bankaccount.personalaccount.jpa;

import org.ilopes.bankaccount.personalaccount.AccountNumber;
import org.ilopes.bankaccount.personalaccount.Operation;
import org.ilopes.bankaccount.personalaccount.TransactionNumber;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;
import java.util.Optional;

@Repository
interface OperationsJpaRepository extends PagingAndSortingRepository<Operation<?>, TransactionNumber> {
    Optional<Operation<?>> findByTransactionNumber(TransactionNumber transactionNumber);
    Set<Operation<?>> findByAccountNumber(AccountNumber accountNumber);
}
