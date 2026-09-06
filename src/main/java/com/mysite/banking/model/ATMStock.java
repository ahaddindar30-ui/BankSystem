package com.mysite.banking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "atm_stack")
@Getter
@Setter
@ToString
public class ATMStock {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "atmStock_sequence")
    @SequenceGenerator(name = "atmStock_sequence" , sequenceName = "hibernate_atm_sqe", allocationSize = 1)
    private Integer id;

    private int denomination;
    private int quantity;
}
