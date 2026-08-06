package com.mysite.BankSystem.model;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Customer {
    private String name;
    private String number;
    private String email;
    private CustomerType type;
    private final Integer id;
    private boolean deleted;


    private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);

    public Customer(String name, String number, String email, CustomerType type) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.type = type;
        this.id = ID_COUNTER.getAndIncrement();
        this.deleted = false;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getId() {
        return id;
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
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type + '\'';
    }
}
