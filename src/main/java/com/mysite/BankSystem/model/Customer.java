package com.mysite.BankSystem.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
@JsonTypeInfo(use= JsonTypeInfo.Id.NAME,property = "type")
@JsonSubTypes({
            @JsonSubTypes.Type(value = LegalCustomer.class , name = "LEGAL"),
            @JsonSubTypes.Type(value = RealCustomer.class , name = "REAL")

})
public abstract class Customer implements Serializable {
    private String name;
    private String number;
    private String email;
    private final CustomerType type;
    @JsonIgnore
    private final Integer id;
    private boolean deleted;


    private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);

    public Customer(CustomerType type) {
        this.id = ID_COUNTER.getAndIncrement();
        this.type = type;
    }

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
