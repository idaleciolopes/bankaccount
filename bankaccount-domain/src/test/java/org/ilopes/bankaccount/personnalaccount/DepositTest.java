package org.ilopes.bankaccount.personnalaccount;

import org.ilopes.bankaccount.personalaccount.AccountNumber;
import org.ilopes.bankaccount.personalaccount.Deposit;
import org.ilopes.bankaccount.personalaccount.TransactionNumber;
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
public class DepositTest {
    private static final TransactionNumber transactionNumber = new TransactionNumber(UUID.randomUUID());
    private static final AccountNumber accountNumber = new AccountNumber(UUID.randomUUID());
    private static final LocalDateTime dateTime = LocalDateTime.now();
    private static final BigDecimal amount = BigDecimal.valueOf(500.52D);

    @Test
    public void When_I_try_to_instantiate_a_deposit_with_negative_amount_Then_instantiation_must_fails() {
        try {
            Deposit.builder().transactionNumber(transactionNumber).accountNumber(accountNumber).dateTime(dateTime)
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
            Deposit.builder().transactionNumber(transactionNumber).accountNumber(accountNumber).dateTime(dateTime)
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
            Deposit.builder().transactionNumber(transactionNumber).accountNumber(accountNumber).dateTime(dateTime)
                    .amount(null).build();
            fail("Expecting instantiation of Deposit with null amount to fail, but succeeds");
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
            assertThat(violations).hasSize(2);
            assertThat(violations.stream().filter(violation->violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotNull.class)).collect(Collectors.toList())).hasSize(2);
            assertThat(violations.stream().filter(violation->violation.getPropertyPath().toString().equals("amount")).collect(Collectors.toList())).hasSize(1);
            assertThat(violations.stream().filter(violation->violation.getPropertyPath().toString().equals("effectiveAmount")).collect(Collectors.toList())).hasSize(1);
        }
    }

    @Test
    public void When_I_try_to_instantiate_a_deposit_with_positive_amount_Then_instantiation_must_succeeds() {
        Deposit deposit = Deposit.builder().transactionNumber(transactionNumber).accountNumber(accountNumber)
                .dateTime(dateTime).amount(amount).build();
        assertThat(deposit.getTransactionNumber()).isEqualTo(transactionNumber);
        assertThat(deposit.getAccountNumber()).isEqualTo(accountNumber);
        assertThat(deposit.getDateTime()).isEqualTo(dateTime);
        assertThat(deposit.getAmount()).isEqualTo(amount);
    }

    @Test
    public void Given_I_have_a_deposit_When_I_get_the_effective_amount_Then_I_get_the_amount_of_the_operation() {
        Deposit deposit = Deposit.builder().transactionNumber(transactionNumber).accountNumber(accountNumber)
                .dateTime(dateTime).amount(amount).build();
        assertThat(deposit.getEffectiveAmount()).isEqualTo(amount);
    }
}
