package com.mysite.BankSystem.mapper;

import com.mysite.BankSystem.dto.CustomerDto;
import com.mysite.BankSystem.dto.LegalCustomerDto;
import com.mysite.BankSystem.dto.RealCustomerDto;
import com.mysite.BankSystem.model.Customer;
import com.mysite.BankSystem.model.LegalCustomer;
import com.mysite.BankSystem.model.RealCustomer;

import java.util.ArrayList;
import java.util.List;

public class CustomerMapper {

    public static List<CustomerDto> mapCustomerDtoList(List<Customer> customerList) {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        for (Customer customer : customerList) {
            customerDtoList.add(mapToCustomerDto(customer));
        }
        return customerDtoList;
    }

    public static CustomerDto mapToCustomerDto(Customer customer) {
        if (customer instanceof LegalCustomer) {
            return mapToLegalCustomerDto((LegalCustomer) customer);
        }else{
            return mapToRealCustomerDto((RealCustomer) customer);
        }
    }

    public static RealCustomerDto mapToRealCustomerDto(RealCustomer realCustomer) {
        RealCustomerDto realCustomerDto = new RealCustomerDto(
                realCustomer.getId(),
                realCustomer.getName(),
                realCustomer.getNumber(),
                realCustomer.getEmail()

        );
        realCustomerDto.setFamily(realCustomer.getFamily());
        realCustomerDto.setNationalCode(realCustomer.getNationalCode());
        return realCustomerDto;
    }


    public static LegalCustomerDto mapToLegalCustomerDto(LegalCustomer legalCustomer) {
        LegalCustomerDto legalCustomerDto = new LegalCustomerDto(
                legalCustomer.getId(),
                legalCustomer.getName(),
                legalCustomer.getNumber(),
                legalCustomer.getEmail()
        );
        legalCustomerDto.setFaxNumber(legalCustomer.getFaxNumber());
        legalCustomerDto.setCompanyRegistration(legalCustomer.getCompanyRegistration());
        return legalCustomerDto;

    }


    public static Customer mapToCustomer(CustomerDto customerDto , Customer customer) {
        if (customerDto instanceof LegalCustomerDto) {
            return mapToLegalCustomer((LegalCustomerDto) customerDto,
                    (LegalCustomer) customer);
        }else{
            return mapToRealCustomer((RealCustomerDto) customerDto,
                    (RealCustomer) customer);
        }
    }

    public static Customer mapToCustomer(CustomerDto customerDto) {
        if (customerDto instanceof LegalCustomerDto) {
            return mapToLegalCustomer((LegalCustomerDto) customerDto,
                    new LegalCustomer(null,null,null));
        }else{
            return mapToRealCustomer((RealCustomerDto) customerDto,
                    new RealCustomer(null,null,null));
        }
    }

    public static RealCustomer mapToRealCustomer(RealCustomerDto realCustomerDto,
                                                 RealCustomer realCustomer) {
        realCustomer.setName(realCustomerDto.getName());
        realCustomer.setNumber(realCustomerDto.getNumber());
        realCustomer.setEmail(realCustomerDto.getEmail());
        realCustomer.setFamily(realCustomerDto.getFamily());
        realCustomer.setNationalCode(realCustomerDto.getNationalCode());
        return realCustomer;
    }


    public static LegalCustomer mapToLegalCustomer(LegalCustomerDto legalCustomerDto,
                                                   LegalCustomer legalCustomer ) {
        legalCustomer.setName(legalCustomerDto.getName());
        legalCustomer.setNumber(legalCustomerDto.getNumber());
        legalCustomer.setEmail(legalCustomerDto.getEmail());
        legalCustomer.setFaxNumber(legalCustomerDto.getFaxNumber());
        legalCustomer.setCompanyRegistration(legalCustomerDto.getCompanyRegistration());
        return legalCustomer;

    }
}
