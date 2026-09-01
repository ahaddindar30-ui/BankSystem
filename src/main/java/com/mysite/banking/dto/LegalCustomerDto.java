package com.mysite.banking.dto;

import com.mysite.banking.model.CustomerType;
import lombok.*;




@Getter
@Setter
@ToString(callSuper = true)
public class LegalCustomerDto extends CustomerDto {
    private String faxNumber;
    private String companyRegistration;
    private String companyName;

    public LegalCustomerDto(Integer id , String name, String number, String email, String password) {
        super(id,name, number, email,password, CustomerType.REAL);
    }

    public LegalCustomerDto(){
        super(CustomerType.REAL);
    }



}
