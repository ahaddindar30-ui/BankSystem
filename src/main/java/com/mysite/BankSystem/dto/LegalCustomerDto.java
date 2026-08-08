package com.mysite.BankSystem.dto;

import com.mysite.BankSystem.model.CustomerType;

public class LegalCustomerDto extends CustomerDto {
    private String faxNumber;
    private String companyRegistration;

    public LegalCustomerDto(Integer id , String name, String number, String email) {
        super(id ,name, number, email, CustomerType.LEGAL);
    }


    @Override
    public String toString() {
        return super.toString() +
                ", faxNumber='" + faxNumber + '\'' +
                ", registration='" + companyRegistration + '\'' +
                '}';
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
}
