package com.mysite.banking.view.component;

import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.dto.LegalCustomerDto;


import java.util.function.Function;


public class LegalCustomerUI extends AbstractCustomerUI {
    public LegalCustomerUI() {
        super();
    }

    @Override
    public CustomerDto additionalGenerateCustomer(String name, String number, String email, String password) {
        String fax = scannerWrapper.getUserInput("Enter your fax number: ", Function.identity());
        String companyRegistration = scannerWrapper.getUserInput("Enter your company Registration: ", Function.identity());
        LegalCustomerDto legalCustomer = new LegalCustomerDto(null ,name, number, email,password);
        legalCustomer.setFaxNumber(fax);
        legalCustomer.setCompanyRegistration(companyRegistration);
        return legalCustomer;
    }

    @Override
    public void editCustomer(CustomerDto customer) {
        LegalCustomerDto legalCustomer = (LegalCustomerDto) customer;
        String name = scannerWrapper.getUserInput("Enter your new name: ", Function.identity());
        customer.setName(name);
        String number = scannerWrapper.getUserInput("Enter your new number: ", Function.identity());
        customer.setNumber(number);
        String email = scannerWrapper.getUserInput("Enter your new email: ", Function.identity());
        customer.setEmail(email);
        String fax = scannerWrapper.getUserInput("Enter your new fax number: ", Function.identity());
        legalCustomer.setFaxNumber(fax);
        String companyRegistration = scannerWrapper.getUserInput("Enter your new company Registration: ", Function.identity());
        legalCustomer.setCompanyRegistration(companyRegistration);
    }


}
