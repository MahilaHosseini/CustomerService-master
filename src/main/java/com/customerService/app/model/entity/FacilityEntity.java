package com.customerService.app.model.entity;

import com.customerService.app.dto.FacilityType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class FacilityEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private FacilityType facilityType;
    private BigDecimal amount;
    private String approvalState;


    public FacilityEntity(){}
    public FacilityEntity(FacilityType facilityType, BigDecimal amount ,String approvalState) {
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
