package com.mysite.BankSystem.model;

public class Customer {
    private String name;
    private String number;
    private String email;
    private CustomerType type;

    public Customer(String name, String number, String email, CustomerType type) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.type = type;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type + '\'' ;
    }
}
