package com.mysite.banking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true)
public class RealCustomer extends Customer implements Serializable {
    private String family;
    private String nationalCode;
    @JsonFormat(shape =  JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")
    private Date birthday;

    public RealCustomer(String name, String number, String email) {
        super(name, number, email, CustomerType.REAL);
    }
    public RealCustomer() {
        super( CustomerType.REAL);
    }



    @Override
    public boolean equals(Object obj) {
        return obj instanceof RealCustomer &&
                ((RealCustomer) obj).getEmail().equals(getEmail())&&
                ((RealCustomer) obj).getNationalCode()
                        .equals(getNationalCode());
    }



}
