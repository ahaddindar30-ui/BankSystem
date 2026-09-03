package com.mysite.banking.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Currency;
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Amount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "amount_sequence")
    @SequenceGenerator(name = "amount_sequence",sequenceName = "hibernate_amount_seq",allocationSize=1)
    private Integer id;
    private Currency currency;
    @Column(name = "amount_value")
    private BigDecimal value;

    @Version
    private long version;

    public Amount(Currency currency, BigDecimal value) {
        this.currency = currency;
        this.value = value;
    }
}
