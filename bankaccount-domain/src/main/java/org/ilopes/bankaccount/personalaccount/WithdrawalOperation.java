package org.ilopes.bankaccount.personalaccount;

import org.ilopes.bankaccount.ddd.DDD;
import org.ilopes.bankaccount.ddd.RestrictedUsage;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * This entity describes a withdrawal operation in the operation log.
 */
@DDD.Entity
public class WithdrawalOperation extends Operation<WithdrawalOperation> {
    public WithdrawalOperation(@NotNull TransactionNumber transactionNumber, @NotNull AccountNumber accountNumber,
                               @NotNull LocalDateTime date, @NonNullPositive BigDecimal amount) {
        super(WithdrawalOperation.class, transactionNumber, accountNumber, date, amount);
    }

    @RestrictedUsage.ForJpaOnly
    protected WithdrawalOperation() {
        super(WithdrawalOperation.class);
    }


    /**
     * Builds an operation to be registerred on operation log from a deposit order and a transaction number.
     *
     * @param transactionNumber the transaction number.
     * @param withdrawalOperation the order to be kept.
     * @return a deposit operation to be storred on operation log.
     */
    public static WithdrawalOperation buildFromOrder(TransactionNumber transactionNumber, WithdrawalOrder withdrawalOperation) {
        return builder().transactionNumber(transactionNumber).accountNumber(withdrawalOperation.getAccountNumber())
                .dateTime(withdrawalOperation.getOperationDateTime()).amount(withdrawalOperation.getAmount()).build();
    }

    /**
     * ${inheritDoc}
     */
    @Override
    @NotNull
    public BigDecimal effectiveAmount() {
        return getAmount().negate();
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Operation.Builder<Builder, WithdrawalOperation> {
        protected Builder() {
            super(Builder.class);
        }

        @Override
        public WithdrawalOperation build() {
            return new WithdrawalOperation(getTransactionNumber(), getAccountNumber(), getDateTime(), getAmount());
        }
    }
}
