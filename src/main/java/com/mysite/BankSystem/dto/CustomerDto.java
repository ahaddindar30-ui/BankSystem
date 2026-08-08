package com.mysite.BankSystem.dto;

import com.mysite.BankSystem.model.CustomerType;

public abstract class CustomerDto {

    private String name;
    private String number;
    private String email;
    private final CustomerType type;
    private final Integer id;

    public CustomerDto(Integer id ,String name, String number, String email, CustomerType type) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.type = type;
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public CustomerType getType() {
        return type;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type + '\'';
    }
}
