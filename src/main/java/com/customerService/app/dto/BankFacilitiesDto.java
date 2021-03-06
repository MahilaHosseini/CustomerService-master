package com.customerService.app.dto;

import java.math.BigDecimal;

public class BankFacilitiesDto {
    private FacilityType facilityType;
    private BigDecimal amount;
    private String accountNumber;
    private String taskId;
    private String approve;

    public BankFacilitiesDto() {
    }

    public BankFacilitiesDto(String facilityType, BigDecimal amount, String accountNumber) {

        this.facilityType = FacilityType.valueOf(facilityType);
        this.amount = amount;
        this.accountNumber = accountNumber;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public FacilityType getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
