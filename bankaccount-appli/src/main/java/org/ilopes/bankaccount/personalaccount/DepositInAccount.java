package org.ilopes.bankaccount.personalaccount;

import org.ilopes.bankaccount.ddd.DDD;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.springframework.stereotype.Service;
import javax.validation.constraints.NotNull;

@DDD.ApplicationService(usRef = "US1", usDescription = "In order to save money As a bank client I want to make a deposit in my account")
@Service
public class DepositInAccount {
    private AccountStatuses accountStatuses;
    private Operations operations;
    private TransactionNumberProvider transactionNumberProvider;

    DepositInAccount(AccountStatuses accountStatuses, Operations operations, TransactionNumberProvider transactionNumberProvider) {
        this.accountStatuses = accountStatuses;
        this.operations = operations;
        this.transactionNumberProvider = transactionNumberProvider;
    }

    @Transactional
    public void depositInAccount(@NotNull DepositOrder depositOrder) {
        // First we look for the account and try to update its status
        Optional<AccountStatus> accountStatus = accountStatuses.findByAccountNumber(depositOrder.getAccountNumber());
        accountStatus.orElseThrow(()->new ForbiddenOperationException("unknown account")).depose(depositOrder);
        TransactionNumber transactionNumber = transactionNumberProvider.giveTransactionNumberForNewTransaction();
        operations.registerOperation(DepositOperation.buildFromOrder(transactionNumber, depositOrder));
    }
}
