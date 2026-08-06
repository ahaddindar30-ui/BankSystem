package com.mysite.BankSystem.model;

public class LegalCustomer extends Customer {
    private String faxNumber;
    private String companyRegistration;

    public LegalCustomer(String name, String number, String email) {
        super(name, number, email, CustomerType.LEGAL);
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getCompanyRegistration() {
        return companyRegistration;
    }

    public void setCompanyRegistration(String companyRegistration) {
        this.companyRegistration = companyRegistration;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LegalCustomer &&
                ((LegalCustomer) obj).getCompanyRegistration().equals(getCompanyRegistration());
    }



    @Override
    public String toString() {
        return super.toString() +
                ", faxNumber='" + faxNumber + '\'' +
                ", registration='" + companyRegistration + '\'' +
                '}';
    }
}
