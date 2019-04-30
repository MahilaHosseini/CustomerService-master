package com.customerService.app.dto;

public class CallNumberDto {
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

    public void setType(String type) {
        this.type = Type.valueOf(type);
    }

    public String getType() {
        return type.name();
    }
    
}
