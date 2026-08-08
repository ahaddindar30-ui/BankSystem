package com.mysite.BankSystem.dto;

import com.mysite.BankSystem.model.CustomerType;

public class RealCustomerDto extends CustomerDto{
    private String family;
    private String nationalCode;

    public RealCustomerDto(Integer id ,String name, String number, String email) {
        super(id,name, number, email, CustomerType.REAL);
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", family='" + family + '\'' +
                ", nationalCode='" + nationalCode + '\'' +
                '}';
    }
}
