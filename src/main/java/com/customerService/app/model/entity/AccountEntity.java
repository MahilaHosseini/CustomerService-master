package com.customerService.app.model.entity;

import com.customerService.app.controller.MapTo;
import com.customerService.app.dto.FacilityDto;
import com.customerService.app.dto.TransactionDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class AccountEntity {

    @Id
    @GeneratedValue
    private Integer id;
    @Version
    private Integer version;
    @Column(unique = true)
    private String accountNumber;
    private BigDecimal accountAmount;
    private Date openingDate = new Date();
    @OneToMany(cascade = CascadeType.ALL)
    @MapTo(targetEntity = TransactionDto.class)
    private List<TransactionEntity> transactions;
    @OneToMany(cascade = CascadeType.ALL)
    @MapTo(targetEntity = FacilityDto.class)
    private List<FacilityEntity> facilities;
    private BigDecimal profit = new BigDecimal(0);
    private BigDecimal minimumOfTheDay ;
    private BigDecimal minimumOfMonth ;

    public AccountEntity(String accountNumber, BigDecimal accountAmount) {
        this.accountNumber = accountNumber;
        this.accountAmount = accountAmount;
        minimumOfTheDay = accountAmount;
        minimumOfMonth = accountAmount;
    }


    public AccountEntity() {

    }

    public List<FacilityEntity> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<FacilityEntity> facilities) {
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

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }
    public List<TransactionEntity> addTransaction(TransactionEntity transactionEntity) {
        transactions.add(transactionEntity);
        return transactions;
    }
    public List<FacilityEntity> addFacility(FacilityEntity facilityEntity) {
        facilities.add(facilityEntity);
        return facilities;
    }
    public void setTransactions(List<TransactionEntity> transactions) {
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
