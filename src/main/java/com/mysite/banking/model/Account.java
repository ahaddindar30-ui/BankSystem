package com.mysite.banking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "account")
@Getter
@Setter
@ToString
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
    @SequenceGenerator(name = "account_sequence", sequenceName = "hibernate_account_seq", allocationSize = 1)
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Amount balance;
    private Integer customerId;
    private boolean deleted;

    public Account() {
        this.deleted = false;
    }

    public Account(Amount balance, Integer customerId) {
        this.balance = balance;
        this.customerId = customerId;
        this.deleted = false;
    }
}
