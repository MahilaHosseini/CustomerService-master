package com.customerService.app.dto;

public enum FacilityType {

    CarLoan("وام خرید خودرو"),
    LandLoan("وام خرید زمین"),
    HouseLoan("وام مسکن"),
    MarriageLoan("وام ازدواج"),
    BenefitFreeLoan("وام قرض الحسنه");
    private String value;

    FacilityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
