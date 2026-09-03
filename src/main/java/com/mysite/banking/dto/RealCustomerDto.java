package com.mysite.banking.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.mysite.banking.model.CustomerType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true)
public class RealCustomerDto extends CustomerDto {
    private String family;
    private String nationalCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date birthday;

    public RealCustomerDto(Integer id, String name, String number, String email, String password) {
        super(id, name, number, email, password, CustomerType.REAL);
    }

    public RealCustomerDto() {
        super(CustomerType.REAL);
    }


}
