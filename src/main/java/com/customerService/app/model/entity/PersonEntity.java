package com.customerService.app.model.entity;

import javax.persistence.*;
import java.util.List;


@Entity
public abstract class PersonEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @Version
    private Integer version ;
    private String name;
    private String eMailAddress;
    private String address;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CallNumberEntity> numbers;
    @OneToMany(cascade = CascadeType.ALL)
    private List<AccountEntity> accountEntities;

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

    public List<CallNumberEntity> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<CallNumberEntity> numbers) {
        this.numbers = numbers;
    }

    public List<AccountEntity> getAccountEntities() {
        return accountEntities;
    }

    public void setAccountEntities(List<AccountEntity> accountEntities) {
        this.accountEntities = accountEntities;
    }

    public void addAccountEntity(AccountEntity accountEntity) {
        this.accountEntities.add( accountEntity);
    }
}

