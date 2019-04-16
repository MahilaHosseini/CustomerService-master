package com.customerService.app.dto;

public enum TransactionType {
    Deposit("واریز"),
    Transfer("انتقال وجه"),
    Removal("برداشت");
    private String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
