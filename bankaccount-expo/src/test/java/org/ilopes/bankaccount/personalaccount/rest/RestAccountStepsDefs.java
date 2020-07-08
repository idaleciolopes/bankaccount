package org.ilopes.bankaccount.personalaccount.rest;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.cucumber.spring.CucumberContextConfiguration;
import org.ilopes.bankaccount.BankAccountApplication;
import org.ilopes.bankaccount.personalaccount.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeEvent;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.ilopes.bankaccount.personalaccount.AccountStepDefsUtils.withdraw;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = BankAccountApplication.class, loader = SpringBootContextLoader.class)
@ComponentScan(basePackages = "org.ilopes.bankaccount.personalaccount")
@CucumberContextConfiguration
public class RestAccountStepsDefs implements En, InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(RestAccountStepsDefs.class);
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AccountStatuses accountStatuses;
    @Autowired
    private Operations operations;
    @Autowired
    private DbInitializer dbInitializer;

    private MockMvc mockMvc;
    private AccountNumber currentAccountNumber;
    private AccountStepDefsUtils.OperationStatus lastOperationStatus;
    private MvcResult lastResult;

    public RestAccountStepsDefs() {
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
        });

        When("^I deposit (\\d+) on it$", (Integer amountDeposited) -> {
            lastOperationStatus = deposit(currentAccountNumber, BigDecimal.valueOf(amountDeposited));
        });
        When("^I deposit -(\\d+) on it$", (Integer amountDeposited) -> {
            lastOperationStatus = deposit(currentAccountNumber, BigDecimal.valueOf(-amountDeposited));
        });
        When("^I withdraw (\\d+) from it$", (Integer amountWithdrawn) -> {
            lastOperationStatus = withdraw(currentAccountNumber, BigDecimal.valueOf(amountWithdrawn));
        });
        When("^I withdraw -(\\d+) from it$", (Integer amountWithdrawn) -> {
            lastOperationStatus = withdraw(currentAccountNumber, BigDecimal.valueOf(-amountWithdrawn));
        });

        Then("^I should have a balance of (\\d+)$", (Integer expectedBalance) -> {
            LOG.info("Looking for balance of my account");
            LOG.info("Expected balance is {}", expectedBalance);
            LOG.info("Current balance is {}", accountStatuses.findByAccountNumber(currentAccountNumber).get().getBalance());
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
            assertThat(accountStatuses.findByAccountNumber(currentAccountNumber)).isNotPresent();
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

    public AccountStepDefsUtils.OperationStatus deposit(AccountNumber accountNumber, BigDecimal amount) {
        String uri = format("/apis/rest/account/%s/actions/deposit/%s", accountNumber.asString(), amount.toString());
        LOG.info("Sending Request to uri {}", uri);
        try {
            lastResult = mockMvc.perform(
                    MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                    .andReturn();
            switch (lastResult.getResponse().getStatus()) {
                case 201:
                    return new AccountStepDefsUtils.OperationStatus(false, false);
                case 451:
                    return new AccountStepDefsUtils.OperationStatus(true, false);
                case 400:
                    return new AccountStepDefsUtils.OperationStatus(false, true);
                default:
                    throw new RuntimeException("Unexpected status : " + lastResult.getResponse().getStatus());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public AccountStepDefsUtils.OperationStatus withdraw(AccountNumber accountNumber, BigDecimal amount) {
        String uri = format("/apis/rest/account/%s/actions/withdraw/%s", accountNumber.asString(), amount.toString());
        LOG.info("Sending Request to uri {}", uri);
        try {
            lastResult = mockMvc.perform(
                    MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                    .andReturn();
            switch (lastResult.getResponse().getStatus()) {
                case 201:
                    return new AccountStepDefsUtils.OperationStatus(false, false);
                case 451:
                    return new AccountStepDefsUtils.OperationStatus(true, false);
                case 400:
                    return new AccountStepDefsUtils.OperationStatus(false, true);
                default:
                    throw new RuntimeException("Unexpected status : " + lastResult.getResponse().getStatus());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void afterPropertiesSet() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
}
