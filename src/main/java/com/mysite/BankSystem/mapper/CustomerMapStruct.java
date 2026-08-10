package com.mysite.BankSystem.mapper;

import com.mysite.BankSystem.dto.CustomerDto;
import com.mysite.BankSystem.dto.LegalCustomerDto;
import com.mysite.BankSystem.dto.RealCustomerDto;
import com.mysite.BankSystem.model.Customer;
import com.mysite.BankSystem.model.LegalCustomer;
import com.mysite.BankSystem.model.RealCustomer;
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
        }else{
            return mapToRealCustomerDto((RealCustomer) customer);
        }
    }

    RealCustomerDto mapToRealCustomerDto(RealCustomer realCustomer);


    LegalCustomerDto mapToLegalCustomerDto(LegalCustomer legalCustomer);


    default  Customer mapToCustomer(CustomerDto customerDto , Customer customer) {
        if (customerDto instanceof LegalCustomerDto) {
            return mapToLegalCustomer((LegalCustomerDto) customerDto,
                    (LegalCustomer) customer);
        }else{
            return mapToRealCustomer((RealCustomerDto) customerDto,
                    (RealCustomer) customer);
        }
    }

    default  Customer mapToCustomer(CustomerDto customerDto) {
        if (customerDto instanceof LegalCustomerDto) {
            return mapToLegalCustomer((LegalCustomerDto) customerDto,
                    new LegalCustomer(null,null,null));
        }else{
            return mapToRealCustomer((RealCustomerDto) customerDto,
                    new RealCustomer(null,null,null));
        }
    }
    @Mapping(ignore = true , target = "id")
    RealCustomer mapToRealCustomer(RealCustomerDto realCustomerDto,
                                                @MappingTarget RealCustomer realCustomer);

    @Mapping(ignore = true , target = "id")
    LegalCustomer mapToLegalCustomer(LegalCustomerDto legalCustomerDto,
                                                  @MappingTarget LegalCustomer legalCustomer );
}
