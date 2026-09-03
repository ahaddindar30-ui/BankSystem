package com.mysite.banking.mapper;

import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.dto.LegalCustomerDto;
import com.mysite.banking.dto.RealCustomerDto;
import com.mysite.banking.model.Customer;
import com.mysite.banking.model.LegalCustomer;
import com.mysite.banking.model.RealCustomer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface CustomerMapStruct {

    List<CustomerDto> mapCustomerDtoList(List<Customer> customerList);

    default CustomerDto mapToCustomerDto(Customer customer) {
        if (customer instanceof LegalCustomer) {
            return mapToLegalCustomerDto((LegalCustomer) customer);
        } else {
            return mapToRealCustomerDto((RealCustomer) customer);
        }
    }

    RealCustomerDto mapToRealCustomerDto(RealCustomer realCustomer);


    LegalCustomerDto mapToLegalCustomerDto(LegalCustomer legalCustomer);


    default Customer mapToCustomer(CustomerDto customerDto, Customer customer) {
        if (customerDto instanceof LegalCustomerDto) {
            return mapToLegalCustomer((LegalCustomerDto) customerDto,
                    (LegalCustomer) customer);
        } else {
            return mapToRealCustomer((RealCustomerDto) customerDto,
                    (RealCustomer) customer);
        }
    }

    default Customer mapToCustomer(CustomerDto customerDto) {
        if (customerDto instanceof LegalCustomerDto) {
            return mapToLegalCustomer((LegalCustomerDto) customerDto,
                    new LegalCustomer(null, null, null));
        } else {
            return mapToRealCustomer((RealCustomerDto) customerDto,
                    new RealCustomer(null, null, null));
        }
    }

    @Mapping(target = "id", ignore = true)
    RealCustomer mapToRealCustomer(RealCustomerDto realCustomerDto,
                                   @MappingTarget RealCustomer realCustomer);

    @Mapping(target = "id", ignore = true)
    LegalCustomer mapToLegalCustomer(LegalCustomerDto legalCustomerDto,
                                     @MappingTarget LegalCustomer legalCustomer);
}
