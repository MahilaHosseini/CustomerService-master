package com.customerService.app.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;


public class FacilityDto {

    private Integer id;
    private FacilityType facilityType;
    private BigDecimal amount;
    private String approvalState;

    public FacilityDto(){}
    public FacilityDto(FacilityType facilityType, BigDecimal amount , String approvalState) {
        this.facilityType = facilityType;
        this.amount = amount;
        this.approvalState = approvalState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getApprovalState() {
        return approvalState;
    }

    public void setApprovalState(String approvalState) {
        this.approvalState = approvalState;
    }
}
