package com.mysite.banking.view.component;

import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.dto.RealCustomerDto;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

public class RealCustomerUI extends AbstractCustomerUI {
    public RealCustomerUI() {
        super();
    }

    @Override
    public CustomerDto additionalGenerateCustomer(String name, String number, String email, String password) {
        String family = scannerWrapper.getUserInput("Enter your family: ", Function.identity());
        String nationalCode = scannerWrapper.getUserInput("Enter your nationalCode: ", Function.identity());
        Date birthDate = scannerWrapper.getUserInput("Enter birthDate(dd-MM-yyyy):" ,
                input->{
                    try {
                        return  new SimpleDateFormat("dd-MM-yyyy").parse(input);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                );
        RealCustomerDto realCustomer = new RealCustomerDto(null ,name, number, email,password);
        realCustomer.setFamily(family);
        realCustomer.setNationalCode(nationalCode);
        realCustomer.setBirthday(birthDate);
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
