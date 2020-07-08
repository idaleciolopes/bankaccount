package org.ilopes.bankaccount.personalaccount;

import io.cucumber.datatable.DataTable;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import static org.mockito.Mockito.when;

/**
 * This utility class defines some methods used to test features on accounts. Goal of this method is to share code
 * between tests done on different layers.
 */
public final class AccountStepDefsUtils {
    // <editor-fold desc="Business methods">

    /**
     * Do a deposit.
     *
     * @param log the logger of the current StepDefs class.
     * @param depositInAccount the service responsible to do a deposit.
     * @param accountNumber the number of the account in which we want to deposit money.
     * @param amountDeposited the amount of the deposit.
     */
    public static OperationStatus deposit(Logger log, DepositInAccount depositInAccount, AccountNumber accountNumber, BigDecimal amountDeposited) {
        log.info("Try to deposit {} on my account", amountDeposited);
        boolean forbiddenOperation = false;
        boolean invalidOperation = false;
        try {
            depositInAccount.depositInAccount(new DepositOrder(accountNumber, LocalDateTime.now(), amountDeposited));
        } catch (ForbiddenOperationException ex) {
            forbiddenOperation = true;
        } catch (ConstraintViolationException ex) {
            invalidOperation = true;
        }
        return new OperationStatus(forbiddenOperation, invalidOperation);
    }


    /**
     * Do a withdrawal.
     *
     * @param log the logger of the current StepDefs class.
     * @param withdrawalFromAccount the service responsible to do a deposit.
     * @param accountNumber the number of the account in which we want to deposit money.
     * @param amountWithdrawn the amount of the deposit.
     */
    public static OperationStatus withdraw(Logger log, WithdrawalFromAccount withdrawalFromAccount, AccountNumber accountNumber, BigDecimal amountWithdrawn) {
        log.info("Try to withdraw {} from my account", amountWithdrawn);
        boolean forbiddenOperation = false;
        boolean invalidOperation = false;
        try {
            withdrawalFromAccount.withdrawFromAccount(new WithdrawalOrder(accountNumber, LocalDateTime.now(), amountWithdrawn));
        } catch (ForbiddenOperationException ex) {
            forbiddenOperation = true;
        } catch (ConstraintViolationException ex) {
            invalidOperation = true;
        }
        return new OperationStatus(forbiddenOperation, invalidOperation);
    }

    // </editor-fold>

    // <editor-fold desc="Parsing gerkhin file">

    /**
     * Parse a date from gerkhins file. I used on tests specifications of date with pattern "today +|- diff" where diff
     * is a number of days. This class loads it and convert it to the appropriate date.
     *
     * @param date the date to parse.
     * @return the parsed date.
     */
    public static LocalDateTime parseDateFromGerkhins(String date) {
        if ("today".equalsIgnoreCase(date)) {
            return LocalDateTime.now();
        } else {
            char operator = date.charAt(6);
            int diff = Integer.parseInt(date.substring(6));
            if ('+' == operator) {
                return LocalDateTime.now().plusDays(diff);
            } else {
                return LocalDateTime.now().minusDays(diff);
            }
        }
    }

    /**
     * Convert a row of a cucumber datatable to an account status.
     *
     * @param row the row of a cucumber datatable to convert.
     * @return the mapped account status.
     */
    public static AccountStatus convertRowToAccountStatus(Map<String, String> row) {
        return new AccountStatus(
                new AccountNumber(row.get("accountNumber")),
                parseDateFromGerkhins(row.get("lastOperationDate")),
                new BigDecimal(row.get("balance")));
    }

    /**
     * Convert a row of a cucumber datatable to an operation.
     *
     * @param row the row of a cucumber datatable to convert.
     * @return the mapped account status.
     */
    public static Operation<?> convertRowToOperation(Map<String, String> row) {
        BigDecimal amount = new BigDecimal(row.get("amount"));
        String type = row.get("type");
        if ("deposit".equals(type)) {
            return new DepositOperation(new TransactionNumber(row.get("transactionNumber")),
                    new AccountNumber(row.get("accountNumber")),
                    parseDateFromGerkhins(row.get("dateTime")),
                    amount);
        } else if ("withdraw".equals(type)) {
            return new WithdrawalOperation(new TransactionNumber(row.get("transactionNumber")),
                    new AccountNumber(row.get("accountNumber")),
                    parseDateFromGerkhins(row.get("dateTime")),
                    amount);
        }
        throw new UnsupportedOperationException(String.format("%s operation not implemented yet", type));
    }

    // </editor-fold>

    /**
     * This class is used to describe an operation status.
     */
    public static class OperationStatus {
        private final boolean forbidden;
        private final boolean invalid;

        public OperationStatus(boolean forbidden, boolean invalid) {
            this.forbidden = forbidden;
            this.invalid = invalid;
        }

        public boolean isForbidden() {
            return forbidden;
        }

        public boolean isInvalid() {
            return invalid;
        }
    }

    private AccountStepDefsUtils() {}
}
