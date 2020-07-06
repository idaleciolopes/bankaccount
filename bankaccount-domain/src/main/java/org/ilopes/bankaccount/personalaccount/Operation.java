package org.ilopes.bankaccount.personalaccount;

import lombok.*;
import org.ilopes.bankaccount.ddd.BaseValidatable;
import org.ilopes.bankaccount.ddd.DDD;
import org.ilopes.bankaccount.ddd.RestrictedUsage;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * This base entity describes an operation. Operations can be Withdraw or Deposits.
 */
@Data @Setter(AccessLevel.NONE) @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@DDD.Entity
public abstract class Operation<O extends Operation<O>> extends BaseValidatable<O> {
    @NotNull @EqualsAndHashCode.Include
    private TransactionNumber transactionNumber;
    @NotNull
    private AccountNumber accountNumber;
    @NotNull
    private LocalDateTime dateTime;
    @NonNullPositive
    private BigDecimal amount;

    public Operation(Class<O> concreteType, @NotNull TransactionNumber transactionNumber, @NotNull AccountNumber accountNumber,
                     @NotNull LocalDateTime dateTime, @NonNullPositive BigDecimal amount) {
        super(concreteType);
        this.transactionNumber = transactionNumber;
        this.accountNumber = accountNumber;
        this.dateTime = dateTime;
        this.amount = amount;
        validate();
    }

    // java:S2637 - number because according to DDD it must be set at object creation and can't be changed, but JPA
    // creates empty beans he populate with the values from the database, so here we have no choice and must initialize
    // all our properties with null.
    @SuppressWarnings("java:S2637")
    @RestrictedUsage.ForJpaOnly
    private Operation(Class<O> concreteType) {
        super(concreteType);
    }

    /**
     * Gets the effective amount of the operation. If operation is a deposit, amount will be a positive amount to
     * improve the balance of the account, if operation is a withdraw it will be a negative amount to decrease the
     * balance of the account.
     *
     * @return the effective amount.
     */
    @NotNull
    public abstract BigDecimal getEffectiveAmount();

    @Getter(AccessLevel.PROTECTED)
    public abstract static class Builder<B extends Builder<B, O>, O extends Operation<O>> {
        private final Class<B> concreteType;
        private TransactionNumber transactionNumber;
        private AccountNumber accountNumber;
        private LocalDateTime dateTime;
        private BigDecimal amount;

        protected Builder(Class<B> concreteType) {
            this.concreteType = concreteType;
        }

        public B transactionNumber(TransactionNumber transactionNumber) {
            this.transactionNumber = transactionNumber;
            return me();
        }

        public B accountNumber(AccountNumber accountNumber) {
            this.accountNumber = accountNumber;
            return me();
        }

        public B dateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return me();
        }

        public B amount(BigDecimal amount) {
            this.amount = amount;
            return me();
        }

        public abstract O build();

        protected B me() {
            return concreteType.cast(this);
        }
    }
}
