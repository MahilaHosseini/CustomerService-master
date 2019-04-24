package com.customerService.app.dto;

import java.math.BigDecimal;

public class UiAccountDto {
    private String code;
    private BigDecimal accountAmount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }
}
