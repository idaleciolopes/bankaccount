package org.ilopes.bankaccount.personalaccount;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.ilopes.bankaccount.ddd.BaseValidatable;
import org.ilopes.bankaccount.ddd.DDD;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * This class is the order passed by client to do a deposit.
 */
@DDD.ValueObject
@Getter @EqualsAndHashCode(callSuper = false) @ToString
public class WithdrawalOrder extends BaseValidatable<WithdrawalOrder> {
    @NotNull
    private final AccountNumber accountNumber;
    @NotNull
    private final LocalDateTime operationDateTime;
    @NonNullPositive
    private final BigDecimal amount;

    public WithdrawalOrder(@NotNull AccountNumber accountNumber, @NotNull LocalDateTime operationDateTime,
                           @NonNullPositive BigDecimal amount) {
        super(WithdrawalOrder.class);
        this.accountNumber = accountNumber;
        this.operationDateTime = operationDateTime;
        this.amount = amount;
        validate();
    }
}
