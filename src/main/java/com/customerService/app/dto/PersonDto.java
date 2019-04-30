package com.customerService.app.dto;

import com.customerService.app.facade.MapTo;
import com.customerService.app.model.entity.AccountEntity;
import com.customerService.app.model.entity.CallNumberEntity;

import java.util.List;

public abstract class PersonDto {

    private Integer id;
    private Integer version ;
    private String name;
    private String eMailAddress;
    private String address;
    @MapTo(targetEntity = CallNumberEntity.class )
    private List<CallNumberDto> numbers;
    @MapTo(targetEntity = AccountEntity.class )
    private List<AccountDto> accounts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() { return version; }

    public void setVersion(Integer version) { this.version = version; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<CallNumberDto> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<CallNumberDto> numbers) {
        this.numbers = numbers;
    }

    public List<AccountDto> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountDto> accounts) {
        this.accounts = accounts;
    }

    public void addAccountDto(AccountDto accountDto) {
        this.accounts.add( accountDto);
    }
}

