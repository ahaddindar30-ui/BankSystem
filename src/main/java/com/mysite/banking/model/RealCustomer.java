package com.mysite.banking.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter
@Setter
@ToString(callSuper = true)
public class RealCustomer extends Customer implements Serializable {
    private String family;
    private String nationalCode;

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
