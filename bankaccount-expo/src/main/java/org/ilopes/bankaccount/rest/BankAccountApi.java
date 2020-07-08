package org.ilopes.bankaccount.rest;

import io.swagger.annotations.*;
import org.ilopes.bankaccount.personalaccount.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.MergedAnnotationCollectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.Set;

/**
 * Controller class for Bank Account REST API.
 */
@Component
@Api
@RestController
@RequestMapping(value = "/apis/rest/", produces = {MediaType.APPLICATION_JSON_VALUE})
public class BankAccountApi {
    private static final Logger LOG = LoggerFactory.getLogger(BankAccountApi.class);
    private DepositInAccount depositInAccount;
    private WithdrawalFromAccount withdrawalFromAccount;
    private GetHistoryForAccount getHistoryForAccount;

    // java:S1144 This constructor will be used by Spring
    @SuppressWarnings("java:1144")
    private BankAccountApi(DepositInAccount depositInAccount, WithdrawalFromAccount withdrawalFromAccount,
                           GetHistoryForAccount getHistoryForAccount) {
        this.depositInAccount = depositInAccount;
        this.withdrawalFromAccount = withdrawalFromAccount;
        this.getHistoryForAccount = getHistoryForAccount;
    }

    @ApiOperation("Do a deposit on an account")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Deposit successfully done"),
            @ApiResponse(code = 451, message = "Operation forbidden by your bank"),
            @ApiResponse(code = 400, message = "Invalid operation")
    })
    @PostMapping("/account/{accountNumber}/actions/deposit/{depositedAmount}")
    @ResponseStatus(HttpStatus.CREATED)
    public void deposit(@ApiParam("Number of the account in which do the deposit") @PathVariable String accountNumber,
                        @ApiParam("Amount of the account to deposit") @PathVariable BigDecimal depositedAmount) {
        LOG.info("Receiving request to do a deposit {} on account {}", depositedAmount, accountNumber);
        DepositOrder depositOrder = new DepositOrder(new AccountNumber(accountNumber), LocalDateTime.now(), depositedAmount);
        depositInAccount.depositInAccount(depositOrder);
    }


    @ApiOperation("Do a withdrawal from an account")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Withdrawal successfully done"),
            @ApiResponse(code = 451, message = "Operation forbidden by your bank"),
            @ApiResponse(code = 400, message = "Invalid operation")
    })
    @PostMapping("/account/{accountNumber}/actions/withdraw/{withdrawnAmount}")
    @ResponseStatus(HttpStatus.CREATED)
    public void withdraw(@ApiParam("Number of the account in which do the deposit") @PathVariable String accountNumber,
                        @ApiParam("Amount of the account to deposit") @PathVariable BigDecimal withdrawnAmount) {
        LOG.info("Receiving request to do a withdraw {} on account {}", withdrawnAmount, accountNumber);
        WithdrawalOrder withdrawalOrder = new WithdrawalOrder(new AccountNumber(accountNumber), LocalDateTime.now(), withdrawnAmount);
        withdrawalFromAccount.withdrawFromAccount(withdrawalOrder);
    }

    @ApiOperation("Gets the history of operations on an account")
    @ApiResponses({
            @ApiResponse(code = 200, message = "History successfully returned"),
            @ApiResponse(code = 451, message = "Unknown account")
    })
    @GetMapping("/account/{accountNumber}/operations/")
    public Set<OperationDetail> getHistoryForAccount(@ApiParam("Number of the account to request") @PathVariable String accountNumber) {
        LOG.info("Receiving a request for history of account {}", accountNumber);
        return getHistoryForAccount.getHistoryForAccount(new AccountNumber(accountNumber))
                .stream().map(OperationDetail::fromOperation).collect(Collectors.toSet());
    }
}
