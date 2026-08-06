package com.mysite.BankSystem.model;

public class RealCustomer extends Customer {
    private String family;
    private String nationalCode;

    public RealCustomer(String name, String number, String email) {
        super(name, number, email, CustomerType.REAL);
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
    public boolean equals(Object obj) {
        return obj instanceof RealCustomer &&
                ((RealCustomer) obj).getEmail().equals(getEmail())&&
                ((RealCustomer) obj).getNationalCode().equals(getNationalCode());
    }


    @Override
    public String toString() {
        return super.toString() +
                ", family='" + family + '\'' +
                ", nationalCode='" + nationalCode + '\'' +
                '}';
    }
}
