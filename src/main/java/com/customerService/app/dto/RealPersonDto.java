package com.customerService.app.dto;

import com.customerService.app.model.entity.CallNumberEntity;
import java.util.ArrayList;
import java.util.List;



public class RealPersonDto extends PersonDto {
    private String lastName;
    private String nationalCode;

    public RealPersonDto() {
    }


    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
    }

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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<CallNumberDto> getNumbers() {
        return super.getNumbers();
    }

    public void setNumbers(List<CallNumberDto> numbers) {
        super.setNumbers(numbers);
    }

    public String geteMailAddress() {

        return super.geteMailAddress();
    }
    public List<AccountDto> getAccounts() {
        return super.getAccounts();
    }

    public void setAccounts(List<AccountDto> accounts) {
        super.setAccounts(accounts);
    }
    public void addAccountDto(AccountDto accountDto) {
        super.addAccountDto(accountDto);
    }
    public void seteMailAddress(String eMailAddress) {
        super.seteMailAddress(eMailAddress);
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getAddress() {
        return super.getAddress();
    }

    public void setAddress(String address) {
        super.setAddress(address);
    }

}
