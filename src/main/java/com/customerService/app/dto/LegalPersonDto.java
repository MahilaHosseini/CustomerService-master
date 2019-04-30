package com.customerService.app.dto;

import com.customerService.app.model.entity.AccountEntity;
import com.customerService.app.model.entity.CallNumberEntity;
import com.customerService.app.model.entity.PersonEntity;

import java.util.Date;
import java.util.List;


public class LegalPersonDto extends PersonEntity {
    private String registrationCode;
    private Date openingDate;

    public Integer getID() {
        return super.getId();
    }

    public void setId(Integer id) {
        super.setId(id);
    }

    public Integer getVersion() {
        return super.getVersion();
    }

    public void setVersion(Integer version) {
        super.setVersion(version);
    }

    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
    }

    public List<CallNumberEntity> getNumbers() {

        return super.getNumbers();
    }

    public List<AccountEntity> getAccounts() {
        return super.getAccounts();
    }

    public void setAccounts(List<AccountEntity> accounts) {
        super.setAccounts(accounts);
    }
    public void addAccountEntity(AccountEntity accountEntity) {
        super.addAccountEntity(accountEntity);
    }
    public void setNumbers(List<CallNumberEntity> numbers) {
        super.setNumbers(numbers);
    }

    public String geteMailAddress() {
        return super.geteMailAddress();
    }

    public void seteMailAddress(String eMailAddress) {

        super.seteMailAddress(eMailAddress);
    }

    public String getAddress() {
        return super.getAddress();
    }

    public void setAddress(String address) {

        super.setAddress(address);
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }
}
