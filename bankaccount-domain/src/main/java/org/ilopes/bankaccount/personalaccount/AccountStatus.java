package org.ilopes.bankaccount.personalaccount;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ilopes.bankaccount.ddd.BaseValidatable;
import org.ilopes.bankaccount.ddd.DDD;
import org.ilopes.bankaccount.ddd.RestrictedUsage;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * This entity describes a personal account.
 */
@DDD.Entity
@Getter @Setter @ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AccountStatus extends BaseValidatable<AccountStatus> {
    @EqualsAndHashCode.Include
    @NotNull
    private AccountNumber number;
    @NotNull
    private LocalDateTime lastOperationDate;
    @NotNull
    private BigDecimal balance;

    public AccountStatus(@NotNull AccountNumber number, LocalDateTime lastOperationDate, BigDecimal balance) {
        super(AccountStatus.class);
        this.number = number;
        this.lastOperationDate = lastOperationDate;
        this.balance = balance;
        validate();
    }

    @RestrictedUsage.ForJpaOnly
    // java:S2637 - number because according to DDD it must be set at object creation and can't be changed, but JPA
    // creates empty beans he populate with the values from the database, so here we have no choice and must initialize
    // all our properties with null.
    @SuppressWarnings("java:S2637")
    protected AccountStatus() {
        super(AccountStatus.class);
        number = null;
        lastOperationDate = null;
        balance = null;
    }

    /**
     * Update the status of the account for a deposit.
     *
     * @param depositOrder the deposit to do.
     */
    public void depose(@NotNull DepositOrder depositOrder) {
        this.balance = this.balance.add(depositOrder.getAmount());
        this.lastOperationDate = depositOrder.getOperationDateTime();
        validate();
    }

    /**
     * Update if possible the status of the account for a withdrawal.
     *
     * @param withdrawalOrder the operation to realize.
     * @throws ForbiddenOperationException balance is not sufficient for this withdrawal.
     */
    // java:S1130 ForbiddentOperationException is RuntimeException and there's no need to declare it in a throw
    // clauses, but has it is in this method a significant result of method execution I add it to improve
    // readability of the method
    @SuppressWarnings("java:S1130")
    public void withdraw(@NotNull WithdrawalOrder withdrawalOrder) throws ForbiddenOperationException {
        if (this.balance.compareTo(withdrawalOrder.getAmount()) < 0) {
            throw new ForbiddenOperationException("insufficient balance for this operation");
        }
        this.balance = this.balance.subtract(withdrawalOrder.getAmount());
        this.lastOperationDate = withdrawalOrder.getOperationDateTime();
        validate();
    }
}
