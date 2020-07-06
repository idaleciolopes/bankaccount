package org.ilopes.bankaccount.personalaccount;

import org.ilopes.bankaccount.ddd.DDD;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * This entity describes a deposit operation.
 */
@DDD.Entity
public class Deposit extends Operation<Deposit> {
    public Deposit(@NotNull TransactionNumber transactionNumber, @NotNull AccountNumber accountNumber,
                   @NotNull LocalDateTime date, @NonNullPositive BigDecimal amount) {
        super(Deposit.class, transactionNumber, accountNumber, date, amount);
    }

    /**
     * ${inheritDoc}
     */
    @Override
    @NotNull
    public BigDecimal getEffectiveAmount() {
        return getAmount();
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Operation.Builder<Builder, Deposit> {
        protected Builder() {
            super(Builder.class);
        }

        @Override
        public Deposit build() {
            return new Deposit(getTransactionNumber(), getAccountNumber(), getDateTime(), getAmount());
        }
    }
}
