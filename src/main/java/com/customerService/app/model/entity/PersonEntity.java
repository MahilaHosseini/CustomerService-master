package com.customerService.app.model.entity;

import com.customerService.app.facade.MapTo;
import com.customerService.app.dto.AccountDto;
import com.customerService.app.dto.CallNumberDto;

import javax.persistence.*;
import java.util.List;


@Entity
public abstract class PersonEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @Version
    private Integer version;
    private String name;
    private String eMailAddress;
    private String address;
    @OneToMany(cascade = CascadeType.ALL)
    @MapTo(targetEntity = CallNumberDto.class)
    private List<CallNumberEntity> numbers;
    @OneToMany(cascade = CascadeType.ALL)
    @MapTo(targetEntity = AccountDto.class)
    private List<AccountEntity> accounts;

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

    public List<CallNumberEntity> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<CallNumberEntity> numbers) {
        this.numbers = numbers;
    }

    public List<AccountEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountEntity> accounts) {
        this.accounts = accounts;
    }

    public void addAccountEntity(AccountEntity accountEntity) {
        this.accounts.add(accountEntity);
    }
}

