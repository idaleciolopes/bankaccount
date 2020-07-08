package org.ilopes.bankaccount.rest;

import org.ilopes.bankaccount.ddd.DDD;
import lombok.Data;
import org.ilopes.bankaccount.personalaccount.Operation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * This class is a DTO used to propagate detailled informations about an operation on JSON format.
 */
@DDD.Dto
@Data
public class OperationDetail {
    private String transactionNumber;
    private String accountNumber;
    private LocalDateTime dateTime;
    private BigDecimal creditedAmount;
    private BigDecimal debitedAmount;
    private BigDecimal effectiveAmount;

    @JsonCreator
    public OperationDetail(@JsonProperty("transactionNumber") String transactionNumber,
                           @JsonProperty("accountNumber") String accountNumber,
                           @JsonProperty("dateTime") LocalDateTime dateTime,
                           @JsonProperty("creditedAmount") BigDecimal creditedAmount,
                           @JsonProperty("debitedAmount") BigDecimal debitedAmount,
                           @JsonProperty("effectiveAmount") BigDecimal effectiveAmount) {
        this.transactionNumber = transactionNumber;
        this.accountNumber = accountNumber;
        this.dateTime = dateTime;
        this.creditedAmount = creditedAmount;
        this.debitedAmount = debitedAmount;
        this.effectiveAmount = effectiveAmount;
    }

    public static OperationDetail fromOperation(Operation<?> operation) {
        return new OperationDetail(operation.getTransactionNumber().asString(), operation.getAccountNumber().asString(),
                operation.getDateTime(), operation.creditedAmount(), operation.debitedAmount(), operation.effectiveAmount());
    }
}
