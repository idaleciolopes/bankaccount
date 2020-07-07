package org.ilopes.bankaccount.personalaccount;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.cucumber.spring.CucumberContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
public class MockedAccountStepsDefs implements En {
    private static final Logger LOG = LoggerFactory.getLogger(MockedAccountStepsDefs.class);
    private final AccountStatuses accountStatuses = mock(AccountStatuses.class);
    private final Operations operations = mock(Operations.class);
    private final TransactionNumberProvider transactionNumberProvider = mock(TransactionNumberProvider.class);
    private final DepositInAccount sut = new DepositInAccount();
    private Optional<AccountStatus> currentAccountStatus;

    public MockedAccountStepsDefs() {
        when(transactionNumberProvider.giveTransactionNumberForNewTransaction()).thenReturn(new TransactionNumber(UUID.randomUUID()));
        Given("^the following accounts exists on system :$", (DataTable table) -> {
            LOG.info("Configuring existing accounts");
            table.asMaps().stream()
                    .map(MockedAccountStepsDefs::mapToAccountStatus)
                    .forEach(status->addAccountStatusToAccountStatusesMock(accountStatuses, status));
        });
        Given("^the following operations were registered :$", (DataTable table) -> {
            LOG.info("Configuring existing operations");
            Map<AccountNumber, List<Operation<?>>> accountMap = new HashMap();
            table.asMaps().stream().map(MockedAccountStepsDefs::mapToOperation)
                    .forEach(operation->addOperationToAccountMap(accountMap, operation));
            accountMap.keySet().stream().forEach(
                    accountNumber->addOperationToOperationsMock(operations, accountNumber, accountMap.get(accountNumber))
            );
        });
        Given("^I own account \"([^\"]*)\"$", (String accountNumber) -> {
            LOG.info("My account is now {}", accountNumber);
            currentAccountStatus = accountStatuses.findByAccountNumber(new AccountNumber(accountNumber));
        });

        When("^I deposit -*(\\d+) on it$", (Integer amountDeposited) -> {
            LOG.info("Try to deposit {} on my account", amountDeposited);
            currentAccountStatus.ifPresent(status->sut.depositInAccount(new DepositInAccountOrder(status.getNumber(), LocalDateTime.now(), BigDecimal.valueOf(amountDeposited))));
        });

        Then("^I should have a balance of (\\d+)$", (Integer expectedBalance) -> {
            LOG.info("Looking for balance of my account");
            assertThat(currentAccountStatus.get().getBalance()).isEqualTo(BigDecimal.valueOf(expectedBalance));
        });
        Then("^operation should be refused$", () -> {
            LOG.info("Was the operation refused ?");
            assertThat(currentAccountStatus).isNotPresent();
        });
        Then("^account \"([^\"]*)\" doesn't exist$", (String accountNumber) -> {
            LOG.info("Does the account exist ?");
            assertThat(currentAccountStatus).isNotPresent();
        });
        Then("^my account has now (\\d+) operation\\(s\\)$", (Integer operationCount) -> {
            LOG.info("Looking for operations associated with my account");
            assertThat(operations.findByAccountNumber(currentAccountStatus.get().getNumber()).size()).isEqualTo(operationCount);
        });
    }

    private static LocalDateTime getDate(String date) {
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

    private static AccountStatus mapToAccountStatus(Map<String, String> map) {
        return new AccountStatus(
                new AccountNumber(map.get("accountNumber")),
                getDate(map.get("lastOperationDate")),
                new BigDecimal(map.get("balance")));
    }

    private static void addAccountStatusToAccountStatusesMock(AccountStatuses mock, AccountStatus status) {
        LOG.info("Configuring status account={}, lastOperationDate={}, balance={]", status.getNumber(), status.getLastOperationDate(), status.getBalance());
        when(mock.findByAccountNumber(status.getNumber())).thenReturn(Optional.of(status));
    }

    private static Operation<?> mapToOperation(Map<String, String> map) {
        BigDecimal amount = new BigDecimal(map.get("amount"));
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            return new Deposit(new TransactionNumber(map.get("transactionNumber")),
                    new AccountNumber(map.get("accountNumber")),
                    getDate(map.get("dateTime")),
                    amount);
        }
        throw new UnsupportedOperationException("Withdrawal not implemented yet");
    }

    private static void addOperationToAccountMap(Map<AccountNumber, List<Operation<?>>> accountMap, Operation<?> operation) {
        List<Operation<?>> operations = accountMap.get(operation.getAccountNumber());
        if (operations == null) {
            operations = new ArrayList();
            accountMap.put(operation.getAccountNumber(), operations);
        }
        operations.add(operation);
    }

    private static void addOperationToOperationsMock(Operations mock, AccountNumber accountNumber, Collection<Operation<?>> operations) {
        LOG.info("Configuring {} operations for account {}", operations.size(), accountNumber);
        when(mock.findByAccountNumber(accountNumber)).thenReturn(operations);
    }
}
