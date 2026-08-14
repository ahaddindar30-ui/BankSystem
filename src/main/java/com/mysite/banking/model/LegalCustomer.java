package com.mysite.banking.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter
@Setter
@ToString(callSuper = true)
public class LegalCustomer extends Customer implements Serializable {
    private String faxNumber;
    private String companyRegistration;

    public LegalCustomer(String name, String number, String email) {
        super(name, number, email, CustomerType.LEGAL);
    }

    public LegalCustomer() {
        super(CustomerType.LEGAL);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LegalCustomer &&
                ((LegalCustomer) obj).getCompanyRegistration().equals(getCompanyRegistration());
    }



}
