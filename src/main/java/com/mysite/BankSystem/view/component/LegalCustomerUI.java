package com.mysite.BankSystem.view.component;

import com.mysite.BankSystem.model.Customer;
import com.mysite.BankSystem.model.LegalCustomer;

import java.util.function.Function;


public class LegalCustomerUI extends AbstractCustomerUI{
    public LegalCustomerUI() {
        super();
    }

    @Override
    public Customer additionalGenerateCustomer(String name , String number, String email) {
        String fax = scannerWrapper.getUserInput("Enter your fax number: ", Function.identity());
        String companyRegistration = scannerWrapper.getUserInput("Enter your company Registration: ", Function.identity());
        LegalCustomer legalCustomer = new LegalCustomer(name, number, email);
        legalCustomer.setFaxNumber(fax);
        legalCustomer.setCompanyRegistration(companyRegistration);
        return legalCustomer;
    }

    @Override
    public void editCustomer(Customer customer) {
        LegalCustomer legalCustomer = (LegalCustomer) customer;
        String fax = scannerWrapper.getUserInput("Enter your new fax number: ", Function.identity());
        legalCustomer.setFaxNumber(fax);
        String companyRegistration = scannerWrapper.getUserInput("Enter your new company Registration: ", Function.identity());
        legalCustomer.setCompanyRegistration(companyRegistration);
    }


}
