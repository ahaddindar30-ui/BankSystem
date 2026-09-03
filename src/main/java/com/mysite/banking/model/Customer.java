package com.mysite.banking.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.io.Serializable;

@Entity
@Table(name = "customer")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LegalCustomer.class, name = "LEGAL"),
        @JsonSubTypes.Type(value = RealCustomer.class, name = "REAL")

})
@Getter
@Setter
@ToString
public abstract class Customer implements Serializable {
    private String name;
    private String number;
    private String email;

    @Enumerated(EnumType.STRING)
    private CustomerType type;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_sequence")
    @SequenceGenerator(name = "customer_sequence", sequenceName = "hibernate_customer_seq", allocationSize = 1)
    private Integer id;
    private boolean deleted;
    private String password;
    @Version
    private long version;

    protected Customer() {

    }

    public Customer(CustomerType type) {
        this.type = type;
        this.deleted = false;
    }

    public Customer(String name, String number, String email, CustomerType type) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.type = type;
        this.deleted = false;
    }


}
