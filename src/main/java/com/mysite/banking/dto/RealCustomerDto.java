package com.mysite.banking.dto;


import com.mysite.banking.model.CustomerType;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
public class RealCustomerDto extends CustomerDto {
    private String family;
    private String nationalCode;

    public RealCustomerDto(Integer id ,String name, String number, String email) {
        super(id ,name, number, email, CustomerType.REAL);
    }

    public RealCustomerDto(){
        super(CustomerType.REAL);
    }




}
