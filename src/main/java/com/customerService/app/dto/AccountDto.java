package com.customerService.app.dto;

import com.customerService.app.controller.MapTo;
import com.customerService.app.model.entity.FacilityEntity;
import com.customerService.app.model.entity.TransactionEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AccountDto {

    private Integer id;
    private Integer version;
    private String accountNumber;
    private BigDecimal accountAmount;
    private Date openingDate = new Date();
    @MapTo(targetEntity = TransactionEntity.class)
    private List<TransactionDto> transactions;
    @MapTo(targetEntity = FacilityEntity.class)
    private List<FacilityDto> facilities ;
    private BigDecimal profit = new BigDecimal(0);
    private BigDecimal minimumOfTheDay ;
    private BigDecimal minimumOfMonth ;

    public AccountDto(String accountNumber, BigDecimal accountAmount) {
        this.accountNumber = accountNumber;
        this.accountAmount = accountAmount;
        minimumOfTheDay = accountAmount;
        minimumOfMonth = accountAmount;
    }


    public AccountDto() {

    }

    public List<FacilityDto> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<FacilityDto> facilities) {
        this.facilities = facilities;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public List<TransactionDto> getTransactions() {
        return transactions;
    }
    public List<TransactionDto> addTransaction(TransactionDto transactionDto) {
        transactions.add(transactionDto);
        return transactions;
    }
    public List<FacilityDto> addFacility(FacilityDto facilityDto) {
        facilities.add(facilityDto);
        return facilities;
    }
    public void setTransactions(List<TransactionDto> transactions) {
        this.transactions = transactions;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getMinimumOfTheDay() {
        return minimumOfTheDay;
    }

    public void setMinimumOfTheDay(BigDecimal minimumOfTheDay) {
        this.minimumOfTheDay = minimumOfTheDay;
    }

    public BigDecimal getMinimumOfMonth() {
        return minimumOfMonth;
    }

    public void setMinimumOfMonth(BigDecimal minimumOfMonth) {
        this.minimumOfMonth = minimumOfMonth;
    }
}
