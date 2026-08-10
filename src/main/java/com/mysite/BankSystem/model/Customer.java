package com.mysite.BankSystem.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
@JsonTypeInfo(use= JsonTypeInfo.Id.NAME,property = "type")
@JsonSubTypes({
            @JsonSubTypes.Type(value = LegalCustomer.class , name = "LEGAL"),
            @JsonSubTypes.Type(value = RealCustomer.class , name = "REAL")

})
@Getter
@Setter
@ToString
public abstract class Customer implements Serializable {
    private String name;
    private String number;
    private String email;
    private final CustomerType type;
    @JsonIgnore
    private Integer id;
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



}
