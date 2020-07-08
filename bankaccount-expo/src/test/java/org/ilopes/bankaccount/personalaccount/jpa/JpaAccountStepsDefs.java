package org.ilopes.bankaccount.personalaccount.jpa;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.cucumber.spring.CucumberContextConfiguration;
import org.ilopes.bankaccount.personalaccount.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.ConstraintViolationException;

import static org.ilopes.bankaccount.personalaccount.AccountStepDefsUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = JpaTestsConfig.class, loader = SpringBootContextLoader.class)
@ComponentScan(basePackages = "org.ilopes.bankaccount.personalaccount")
@CucumberContextConfiguration
public class JpaAccountStepsDefs implements En {
    private static final Logger LOG = LoggerFactory.getLogger(JpaAccountStepsDefs.class);
    // Repositories and domain services
    @Autowired
    private AccountStatuses accountStatuses;
    @Autowired
    private Operations operations;
    @Autowired
    private DbInitializer dbInitializer;
    @Autowired
    private TransactionNumberProvider transactionNumberProvider;

    // Application services
    @Autowired
    private DepositInAccount depositInAccount;
    @Autowired
    private WithdrawalFromAccount withdrawalFromAccount;

    // Status
    private AccountNumber currentAccountNumber;
    private Optional<AccountStatus> currentAccountStatus;
    private OperationStatus lastOperationStatus;

    public JpaAccountStepsDefs() {
        Given("^the following accounts exists on system :$", (DataTable table) -> {
            LOG.info("Configuring existing accounts");
            table.asMaps().stream().map(AccountStepDefsUtils::convertRowToAccountStatus)
                    .forEach(dbInitializer::saveAccountStatus);
        });
        Given("^the following operations were registered :$", (DataTable table) -> {
            LOG.info("Configuring existing operations");
            table.asMaps().stream().map(AccountStepDefsUtils::convertRowToOperation)
                    .forEach(dbInitializer::saveOperation);
        });
        Given("^I own account \"([^\"]*)\"$", (String accountNumber) -> {
            LOG.info("My account is now {}", accountNumber);
            currentAccountNumber = new AccountNumber(accountNumber);
            currentAccountStatus = accountStatuses.findByAccountNumber(currentAccountNumber);
        });

        When("^I deposit (\\d+) on it$", (Integer amountDeposited) -> {
            lastOperationStatus = deposit(LOG, depositInAccount, currentAccountNumber, BigDecimal.valueOf(amountDeposited));
        });
        When("^I deposit -(\\d+) on it$", (Integer amountDeposited) -> {
            lastOperationStatus = deposit(LOG, depositInAccount, currentAccountNumber, BigDecimal.valueOf(-amountDeposited));
        });
        When("^I withdraw (\\d+) from it$", (Integer amountWithdrawn) -> {
            lastOperationStatus = withdraw(LOG, withdrawalFromAccount, currentAccountNumber, BigDecimal.valueOf(amountWithdrawn));
        });
        When("^I withdraw -(\\d+) from it$", (Integer amountWithdrawn) -> {
            lastOperationStatus = withdraw(LOG, withdrawalFromAccount, currentAccountNumber, BigDecimal.valueOf(-amountWithdrawn));
        });

        Then("^I should have a balance of (\\d+)$", (Integer expectedBalance) -> {
            LOG.info("Looking for balance of my account");
            assertThat(accountStatuses.findByAccountNumber(currentAccountNumber).get().getBalance().compareTo(BigDecimal.valueOf(expectedBalance))).isEqualTo(0);
        });
        Then("^operation should be refused$", () -> {
            LOG.info("Was the operation refused ?");
            assertThat(lastOperationStatus.isForbidden()).isTrue();
        });
        Then("^operation should be invalid$", () -> {
            LOG.info("Was the operation valid ?");
            assertThat(lastOperationStatus.isInvalid()).isTrue();
        });
        Then("^account \"([^\"]*)\" doesn't exist$", (String accountNumber) -> {
            LOG.info("Does the account exist ?");
            assertThat(currentAccountStatus).isNotPresent();
        });
        Then("^my account has now (\\d+) operation\\(s\\)$", (Integer operationCount) -> {
            LOG.info("Looking for operations associated with my account after success");
            assertThat(operations.findByAccountNumber(currentAccountNumber)).hasSize(operationCount);
        });
        Then("^my account still has (\\d+) operation\\(s\\)$", (Integer operationCount) -> {
            LOG.info("Looking for operations associated with my account after failure");
            assertThat(operations.findByAccountNumber(currentAccountNumber)).hasSize(operationCount);
        });
    }
}
