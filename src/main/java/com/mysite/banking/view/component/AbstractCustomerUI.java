package com.mysite.banking.view.component;

import com.mysite.banking.dto.CustomerDto;

import com.mysite.banking.model.CustomerType;

import com.mysite.banking.util.ScannerWrapper;

import java.util.function.Function;

public abstract class AbstractCustomerUI {
    protected final ScannerWrapper scannerWrapper;

    public AbstractCustomerUI() {
        this.scannerWrapper = ScannerWrapper.getInstance();

    }

    public CustomerDto generateCustomerUI() {
        String name = scannerWrapper.getUserInput("Enter your name: ", Function.identity());
        String number = scannerWrapper.getUserInput("Enter your number: ", Function.identity());
        String email = scannerWrapper.getUserInput("Enter your email: ", Function.identity());
        return additionalGenerateCustomer(name, number, email);
    }


    protected abstract CustomerDto additionalGenerateCustomer(String name, String number, String email);


    public abstract void editCustomer(CustomerDto customer);

    public static AbstractCustomerUI fromCustomerUI(CustomerType type) {
        return switch (type) {
            case REAL -> new RealCustomerUI();
            case LEGAL -> new LegalCustomerUI();
        };
    }

}
