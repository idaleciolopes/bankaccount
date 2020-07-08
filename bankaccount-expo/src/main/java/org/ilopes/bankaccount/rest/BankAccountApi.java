package org.ilopes.bankaccount.rest;

import io.swagger.annotations.*;
import org.ilopes.bankaccount.personalaccount.AccountNumber;
import org.ilopes.bankaccount.personalaccount.DepositInAccount;
import org.ilopes.bankaccount.personalaccount.DepositOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    // java:S1144 This constructor will be used by Spring
    @SuppressWarnings("java:1144")
    private BankAccountApi(DepositInAccount depositInAccount) {
        this.depositInAccount = depositInAccount;
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
}
