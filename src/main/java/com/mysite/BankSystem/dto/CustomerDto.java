package com.mysite.BankSystem.dto;

import com.mysite.BankSystem.model.CustomerType;
import lombok.*;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class CustomerDto {
    private Integer id;
    private String name;
    private String number;
    private String email;
    private final CustomerType type;









}
