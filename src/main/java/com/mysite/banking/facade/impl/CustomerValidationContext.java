package com.mysite.banking.facade.impl;

import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.dto.LegalCustomerDto;
import com.mysite.banking.dto.RealCustomerDto;
import com.mysite.banking.facade.CustomerFacade;
import com.mysite.banking.service.exception.CustomerNotFindException;
import com.mysite.banking.service.exception.ValidationException;
import com.mysite.banking.service.validation.ValidationContext;
import com.mysite.banking.util.RegexValidator;

public class CustomerValidationContext extends ValidationContext<CustomerDto> {
    protected final CustomerFacade customerFacade;

    public  CustomerValidationContext(CustomerFacade customerFacade){
       this.customerFacade = customerFacade;
        //  name validation
        addValidation(customer -> {
            String name = customer.getName();
            if (name == null || name.trim().isEmpty()) {
                throw new ValidationException("Customer name is empty or null");
            }
        });

        //companyName validation
        addValidation(customer -> {
            if (customer instanceof LegalCustomerDto) {
                String companyName = ((LegalCustomerDto) customer).getCompanyName();
                if (companyName == null || companyName.trim().isEmpty()) {
                    throw new ValidationException("Customer companyName is empty or null");
                }
            }
        });
        //  number validation
        addValidation(customer -> {
            String number = customer.getNumber();
            if (!RegexValidator.regexNumber(number) || number.trim().isEmpty()) {
                throw new ValidationException("Invalid format number ");
            }
        });
        //  email validation
        addValidation(customer -> {
            String email = customer.getEmail();

            if (email == null || email.trim().isEmpty()) {
                throw new ValidationException("Email cannot be null or empty");
            }
            try {
                customerFacade.printCustomersByEmail(email);
                throw new ValidationException("Email must not be duplicated");

            } catch (CustomerNotFindException ignored) {

            }

            if (!RegexValidator.regexEmail(email)) {
                throw new ValidationException("Invalid email format");
            }
        });

        //  family validation
        addValidation(customer -> {
            if (customer instanceof RealCustomerDto){
                String family = ((RealCustomerDto)customer).getFamily();
                if (family == null || family.trim().isEmpty()) {
                    throw new ValidationException("Customer family is empty or null");
                }
            }
        });

        //  nationalCode validation
        addValidation(customer -> {
            if (customer instanceof RealCustomerDto){
                String nationalCode = ((RealCustomerDto) customer).getNationalCode();
                if (!RegexValidator.regexNationalCode(nationalCode) || nationalCode.trim().isEmpty()) {
                    throw new ValidationException("Invalid format national code ");
                }
            }
        });

        //  fax validation
        addValidation(customer -> {
            if (customer instanceof LegalCustomerDto){
                String fax = ((LegalCustomerDto) customer).getFaxNumber();
                if (!RegexValidator.regexNumber(fax)|| fax.trim().trim().isEmpty()) {
                    throw new ValidationException("Invalid format fax number ");
                }
            }
        });

        //  companyRegistration validation
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
