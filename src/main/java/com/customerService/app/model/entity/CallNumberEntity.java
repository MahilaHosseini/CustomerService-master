package com.customerService.app.model.entity;

import com.customerService.app.dto.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CallNumberEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private String number;
    private Type type;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getType() {
        return type.name();
    }


    @Override
    public String toString() {
        return type.name() + " Number : " + number;
    }
}
