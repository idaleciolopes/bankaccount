package org.ilopes.bankaccount.personalaccount;

import org.ilopes.bankaccount.ddd.DDD;
import org.ilopes.bankaccount.ddd.RestrictedUsage;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * This entity describes a deposit operation in the operation log.
 */
@DDD.Entity
public class DepositOperation extends Operation<DepositOperation> {
    public DepositOperation(@NotNull TransactionNumber transactionNumber, @NotNull AccountNumber accountNumber,
                            @NotNull LocalDateTime date, @NonNullPositive BigDecimal amount) {
        super(DepositOperation.class, transactionNumber, accountNumber, date, amount);
    }

    @RestrictedUsage.ForJpaOnly
    protected DepositOperation() {
        super(DepositOperation.class);
    }


    /**
     * Builds an operation to be registerred on operation log from a deposit order and a transaction number.
     *
     * @param transactionNumber the transaction number.
     * @param depositOrder the order to be kept.
     * @return a deposit operation to be storred on operation log.
     */
    public static DepositOperation buildFromOrder(TransactionNumber transactionNumber, DepositOrder depositOrder) {
        return builder().transactionNumber(transactionNumber).accountNumber(depositOrder.getAccountNumber())
                .dateTime(depositOrder.getOperationDateTime()).amount(depositOrder.getAmount()).build();
    }

    /**
     * ${inheritDoc}
     */
    @Override
    @NotNull
    public BigDecimal effectiveAmount() {
        return getAmount();
    }

    /**
     * ${inheritDoc}
     */
    @Override
    @NotNull
    public BigDecimal creditedAmount() {
        return getAmount();
    }

    /**
     * ${inheritDoc}
     */
    @Override
    @NotNull
    public BigDecimal debitedAmount() {
        return BigDecimal.ZERO;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Operation.Builder<Builder, DepositOperation> {
        protected Builder() {
            super(Builder.class);
        }

        @Override
        public DepositOperation build() {
            return new DepositOperation(getTransactionNumber(), getAccountNumber(), getDateTime(), getAmount());
        }
    }
}
