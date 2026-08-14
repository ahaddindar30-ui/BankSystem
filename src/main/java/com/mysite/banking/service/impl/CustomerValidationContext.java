package com.mysite.banking.service.impl;

import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.dto.LegalCustomerDto;
import com.mysite.banking.dto.RealCustomerDto;
import com.mysite.banking.service.exception.ValidationException;
import com.mysite.banking.service.validation.ValidationContext;
import com.mysite.banking.util.RegexValidator;

public class CustomerValidationContext extends ValidationContext<CustomerDto> {

    public  CustomerValidationContext(){
        addValidation(customer -> {
            String name = customer.getName();
            if (name == null || name.trim().isEmpty()) {
                throw new ValidationException("Customer name is empty or null");
            }
        });

        addValidation(customer -> {
            String number = customer.getNumber();
            if (!RegexValidator.regexNumber(number) || number.trim().isEmpty()) {
                throw new ValidationException("Invalid format number ");
            }
        });

        addValidation(customer -> {
            String email = customer.getEmail();
            if (!RegexValidator.regexEmail(email) || email.trim().isEmpty()) {
                throw new ValidationException("Invalid format email ");
            }
        });

        addValidation(customer -> {
            if (customer instanceof RealCustomerDto){
                String family = ((RealCustomerDto)customer).getFamily();
                if (family == null || family.trim().isEmpty()) {
                    throw new ValidationException("Customer family is empty or null");
                }
            }
        });
        addValidation(customer -> {
            if (customer instanceof RealCustomerDto){
                String nationalCode = ((RealCustomerDto) customer).getNationalCode();
                if (!RegexValidator.regexNationalCode(nationalCode) || nationalCode.trim().isEmpty()) {
                    throw new ValidationException("Invalid format national code ");
                }
            }
        });

        addValidation(customer -> {
            if (customer instanceof LegalCustomerDto){
                String fax = ((LegalCustomerDto) customer).getFaxNumber();
                if (!RegexValidator.regexNumber(fax)|| fax.trim().trim().isEmpty()) {
                    throw new ValidationException("Invalid format fax number ");
                }
            }
        });
        addValidation(customer -> {
            if (customer instanceof LegalCustomerDto){
                String registration = ((LegalCustomerDto) customer).getCompanyRegistration();
                if (!RegexValidator.regexCompanyRegistration(registration)) {
                    throw new ValidationException("Invalid format company registration  ");
                }
            }
        });
    }


}
