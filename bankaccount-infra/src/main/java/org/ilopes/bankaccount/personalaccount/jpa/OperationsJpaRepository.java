package org.ilopes.bankaccount.personalaccount.jpa;

import org.ilopes.bankaccount.personalaccount.AccountNumber;
import org.ilopes.bankaccount.personalaccount.Operation;
import org.ilopes.bankaccount.personalaccount.TransactionNumber;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@Repository
interface OperationsJpaRepository extends PagingAndSortingRepository<Operation<?>, TransactionNumber> {
    List<Operation<?>> findByAccountNumber(AccountNumber accountNumber);
}
