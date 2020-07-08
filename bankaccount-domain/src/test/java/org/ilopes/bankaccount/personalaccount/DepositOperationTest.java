package org.ilopes.bankaccount.personalaccount;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * This test case just test the main business rules of a deposit.
 */
public class DepositOperationTest {
    private static final TransactionNumber transactionNumber = new TransactionNumber(UUID.randomUUID());
    private static final AccountNumber accountNumber = new AccountNumber(UUID.randomUUID());
    private static final LocalDateTime dateTime = LocalDateTime.now();
    private static final BigDecimal amount = BigDecimal.valueOf(500.52D);

    @Test
    public void When_I_try_to_instantiate_a_deposit_with_negative_amount_Then_instantiation_must_fails() {
        try {
            DepositOperation.builder().transactionNumber(transactionNumber).accountNumber(accountNumber).dateTime(dateTime)
                    .amount(BigDecimal.valueOf(-50.25D)).build();
            fail("Expecting instantiation of Deposit with negative amount to fail, but succeeds");
        } catch (ConstraintViolationException ex) {
            assertThat(ex.getConstraintViolations()).hasSize(1);
            ConstraintViolation<?> violation = ex.getConstraintViolations().iterator().next();
            assertThat(violation.getConstraintDescriptor().getAnnotation()).isInstanceOf(Positive.class);
            assertThat(violation.getPropertyPath().toString()).isEqualTo("amount");
        }
    }

    @Test
    public void When_I_try_to_instantiate_a_deposit_with_zero_amount_Then_instantiation_must_fails() {
        try {
            DepositOperation.builder().transactionNumber(transactionNumber).accountNumber(accountNumber).dateTime(dateTime)
                    .amount(BigDecimal.valueOf(0)).build();
            fail("Expecting instantiation of Deposit with amount of 0 to fail, but succeeds");
        } catch (ConstraintViolationException ex) {
            assertThat(ex.getConstraintViolations()).hasSize(1);
            ConstraintViolation<?> violation = ex.getConstraintViolations().iterator().next();
            assertThat(violation.getConstraintDescriptor().getAnnotation()).isInstanceOf(Positive.class);
            assertThat(violation.getPropertyPath().toString()).isEqualTo("amount");
        }
    }

    @Test
    public void When_I_try_to_instantiate_a_deposit_with_null_amount_Then_instantiation_must_fails() {
        try {
            DepositOperation.builder().transactionNumber(transactionNumber).accountNumber(accountNumber).dateTime(dateTime)
                    .amount(null).build();
            fail("Expecting instantiation of Deposit with null amount to fail, but succeeds");
        } catch (ConstraintViolationException ex) {
            assertThat(ex.getConstraintViolations()).hasSize(1);
            ConstraintViolation<?> violation = ex.getConstraintViolations().iterator().next();
            assertThat(violation.getConstraintDescriptor().getAnnotation()).isInstanceOf(NotNull.class);
            assertThat(violation.getPropertyPath().toString()).isEqualTo("amount");
        }
    }

    @Test
    public void When_I_try_to_instantiate_a_deposit_with_positive_amount_Then_instantiation_must_succeeds() {
        DepositOperation depositOperation = DepositOperation.builder().transactionNumber(transactionNumber).accountNumber(accountNumber)
                .dateTime(dateTime).amount(amount).build();
        assertThat(depositOperation.getTransactionNumber()).isEqualTo(transactionNumber);
        assertThat(depositOperation.getAccountNumber()).isEqualTo(accountNumber);
        assertThat(depositOperation.getDateTime()).isEqualTo(dateTime);
        assertThat(depositOperation.getAmount()).isEqualTo(amount);
    }

    @Test
    public void Given_I_have_a_deposit_When_I_get_the_effective_amount_Then_I_get_the_amount_of_the_operation() {
        DepositOperation depositOperation = DepositOperation.builder().transactionNumber(transactionNumber).accountNumber(accountNumber)
                .dateTime(dateTime).amount(amount).build();
        assertThat(depositOperation.effectiveAmount()).isEqualTo(amount);
    }
}
