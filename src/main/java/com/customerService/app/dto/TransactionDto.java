package com.customerService.app.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;


public class TransactionDto {

    private Integer id;
    private Date transactionDate = new Date();
    private BigDecimal amount;
    private TransactionType transactionType;
    private String destinationAccountNumber;

    public TransactionDto(BigDecimal amount, TransactionType transactionType, String destinationAccountNumber) {
        this.amount = amount;
        this.transactionType = transactionType;
        this.destinationAccountNumber = destinationAccountNumber;
    }
    public TransactionDto(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
