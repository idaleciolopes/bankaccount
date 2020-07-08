package org.ilopes.bankaccount.personalaccount;

import org.ilopes.bankaccount.ddd.DDD;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@DDD.ApplicationService(usRef = "US2", usDescription = "In order to retrieve some money As a bank client I want to make a withdrawal from my account")
@Service
public class WithdrawalFromAccount {
    private AccountStatuses accountStatuses;
    private Operations operations;
    private TransactionNumberProvider transactionNumberProvider;

    WithdrawalFromAccount(AccountStatuses accountStatuses, Operations operations, TransactionNumberProvider transactionNumberProvider) {
        this.accountStatuses = accountStatuses;
        this.operations = operations;
        this.transactionNumberProvider = transactionNumberProvider;
    }

    @Transactional
    public void withdrawFromAccount(@NotNull WithdrawalOrder withdrawalOrder) {
        // First we look for the account and try to update its status
        Optional<AccountStatus> accountStatus = accountStatuses.findByAccountNumber(withdrawalOrder.getAccountNumber());
        accountStatus.orElseThrow(()->new ForbiddenOperationException("unknown account")).withdraw(withdrawalOrder);
        // Then we add the operation to the operation log
        TransactionNumber transactionNumber = transactionNumberProvider.giveTransactionNumberForNewTransaction();
        operations.registerOperation(WithdrawalOperation.buildFromOrder(transactionNumber, withdrawalOrder));
    }
}
