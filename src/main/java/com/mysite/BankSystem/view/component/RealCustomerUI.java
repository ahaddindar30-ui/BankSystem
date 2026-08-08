package com.mysite.BankSystem.view.component;

import com.mysite.BankSystem.dto.CustomerDto;
import com.mysite.BankSystem.dto.RealCustomerDto;


import java.util.function.Function;

public class RealCustomerUI extends AbstractCustomerUI {
    public RealCustomerUI() {
        super();
    }

    @Override
    public CustomerDto additionalGenerateCustomer(String name, String number, String email) {
        String family = scannerWrapper.getUserInput("Enter your family: ", Function.identity());
        String nationalCode = scannerWrapper.getUserInput("Enter your nationalCode: ", Function.identity());
        RealCustomerDto realCustomer = new RealCustomerDto(null ,name, number, email);
        realCustomer.setFamily(family);
        realCustomer.setNationalCode(nationalCode);
        return realCustomer;
    }

    @Override
    public void editCustomer(CustomerDto customer) {
        RealCustomerDto realCustomer = (RealCustomerDto) customer;
        String name = scannerWrapper.getUserInput("Enter your new name: ", Function.identity());
        customer.setName(name);
        String number = scannerWrapper.getUserInput("Enter your new number: ", Function.identity());
        customer.setNumber(number);
        String email = scannerWrapper.getUserInput("Enter your new email: ", Function.identity());
        customer.setEmail(email);
        String family = scannerWrapper.getUserInput("Enter your new family: ", Function.identity());
        realCustomer.setFamily(family);
        String nationalCode = scannerWrapper.getUserInput("Enter your new nationalCode: ", Function.identity());
        realCustomer.setNationalCode(nationalCode);
    }


}
