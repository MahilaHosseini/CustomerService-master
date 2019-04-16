package com.customerService.app.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RealPersonEntity extends PersonEntity {
    private String lastName;
    @Column(unique = true)
    private String nationalCode;

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

    public List<CallNumberEntity> getNumbers() {
        return super.getNumbers();
    }

    public void setNumbers(ArrayList<CallNumberEntity> numbers) {
        super.setNumbers(numbers);
    }

    public String geteMailAddress() {

        return super.geteMailAddress();
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


    @Override
    public String toString() {
        StringBuilder toStr = new StringBuilder("<br>Name: " + super.getName() + "<br>Last Name: "
                + lastName + "<br>E-Mail Address: " + super.geteMailAddress() + "<br>National Code: " + nationalCode
                + "<br>Address: " + super.getAddress());
        for (CallNumberEntity cn : super.getNumbers()) {
            toStr.append("<br>" + cn.toString());
        }
        return toStr.toString();
    }
}
