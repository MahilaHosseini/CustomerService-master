package com.customerService.app.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private List<TransactionEntity> transactionEntities = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<FacilityEntity> facilityEntities = new ArrayList<>();
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

    public List<FacilityEntity> getFacilityEntities() {
        return facilityEntities;
    }

    public void setFacilityEntities(List<FacilityEntity> facilityEntities) {
        this.facilityEntities = facilityEntities;
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

    public List<TransactionEntity> getTransactionEntities() {
        return transactionEntities;
    }
    public List<TransactionEntity> addTransaction(TransactionEntity transactionEntity) {
        transactionEntities.add(transactionEntity);
        return transactionEntities;
    }
    public List<FacilityEntity> addFacility(FacilityEntity facilityEntity) {
        facilityEntities.add(facilityEntity);
        return facilityEntities;
    }
    public void setTransactionEntities(List<TransactionEntity> transactionEntities) {
        this.transactionEntities = transactionEntities;
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
