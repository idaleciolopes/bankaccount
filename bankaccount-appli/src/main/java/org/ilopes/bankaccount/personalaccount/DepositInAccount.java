package org.ilopes.bankaccount.personalaccount;

import org.ilopes.bankaccount.ddd.DDD;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@DDD.ApplicationService(usRef = "US1", usDescription = "In order to save money As a bank client I want to make a deposit in my account")
@Transactional
public class DepositInAccount {
    public void depositInAccount(@NotNull DepositInAccountOrder depositInAccountOrder) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
