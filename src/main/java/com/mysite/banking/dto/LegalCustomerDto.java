package com.mysite.banking.dto;

import com.mysite.banking.model.CustomerType;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
public class LegalCustomerDto extends CustomerDto {
    private String faxNumber;
    private String companyRegistration;

    public LegalCustomerDto(Integer id ,String name, String number, String email) {
        super(id,name, number, email, CustomerType.REAL);
    }

    public LegalCustomerDto(){
        super(CustomerType.REAL);
    }



}
