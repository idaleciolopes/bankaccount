package org.ilopes.bankaccount.personalaccount;

import org.ilopes.bankaccount.ddd.DDD;
import org.springframework.stereotype.Service;

import java.util.Collection;

@DDD.ApplicationService(usRef = "US3", usDescription = "In order check my operations As a bank client I want to see the history of my operations")
@Service
public class GetHistoryForAccount {
    private AccountStatuses accountStatuses;
    private Operations operations;

    GetHistoryForAccount(AccountStatuses accountStatuses, Operations operations) {
        this.accountStatuses = accountStatuses;
        this.operations = operations;
    }

    public Collection<Operation<?>> getHistoryForAccount(AccountNumber accountNumber) {
        if (!accountStatuses.findByAccountNumber(accountNumber).isPresent()) {
            throw new ForbiddenOperationException("Unknown account");
        }
        return operations.findByAccountNumber(accountNumber);
    }
}
