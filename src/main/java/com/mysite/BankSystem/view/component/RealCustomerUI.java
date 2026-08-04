package com.mysite.BankSystem.view.component;

import com.mysite.BankSystem.model.Customer;
import com.mysite.BankSystem.model.RealCustomer;

import java.util.Scanner;
import java.util.function.Function;

public class RealCustomerUI extends AbstractCustomerUI{
    public RealCustomerUI() {
        super();
    }

    @Override
    public Customer additionalGenerateCustomer(String name , String number, String email) {
        String family = scannerWrapper.getUserInput("Enter your family: ", Function.identity());
        String nationalCode = scannerWrapper.getUserInput("Enter your nationalCode: ", Function.identity());
        RealCustomer realCustomer = new RealCustomer(name, number, email);
        realCustomer.setFamily(family);
        realCustomer.setNationalCode(nationalCode);
        return realCustomer;
    }

    @Override
    public void editCustomer(Customer customer) {
        RealCustomer realCustomer = (RealCustomer) customer;
        String family = scannerWrapper.getUserInput("Enter your new family: ", Function.identity());
        realCustomer.setFamily(family);
        String nationalCode = scannerWrapper.getUserInput("Enter your new nationalCode: ", Function.identity());
        realCustomer.setNationalCode(nationalCode);
    }


}
