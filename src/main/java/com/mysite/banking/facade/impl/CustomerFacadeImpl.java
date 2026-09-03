package com.mysite.banking.facade.impl;

import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.facade.CustomerFacade;
import com.mysite.banking.mapper.CustomerMapStruct;
import com.mysite.banking.model.Customer;
import com.mysite.banking.service.CustomerService;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.service.impl.CustomerServiceImpl;
import com.mysite.banking.service.validation.ValidationContext;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class CustomerFacadeImpl implements CustomerFacade {


    private ValidationContext<CustomerDto> validationContext;

    private CustomerService customerService;
    private final CustomerMapStruct customerMapStruct;


    private static final CustomerFacadeImpl INSTANCE;

    public static CustomerFacadeImpl getInstance() {
        return INSTANCE;
    }

    public static CustomerFacadeImpl getInstance(CustomerService customerService) {
        INSTANCE.customerService = customerService;
        INSTANCE.validationContext = new CustomerValidationContext(INSTANCE);
        return INSTANCE;
    }

    static {
        INSTANCE = new CustomerFacadeImpl();
    }

    private CustomerFacadeImpl() {
        this.customerMapStruct = Mappers.getMapper(CustomerMapStruct.class);
        this.customerService = CustomerServiceImpl.getInstance();
        this.validationContext = new CustomerValidationContext(this);
    }


    @Override
    public void deleteCustomerById(Integer id) throws CustomerNotFindException {
        customerService.deleteCustomerById(id);
    }

    @Override
    public List<CustomerDto> printCustomerByFamily(String family) {
        return customerMapStruct.mapCustomerDtoList(
                customerService.printCustomerByFamily(family));
    }

    @Override
    public List<CustomerDto> printCustomersByName(String name) {
        return customerMapStruct.mapCustomerDtoList(
                customerService.printCustomersByName(name));
    }

    @Override
    public void addCustomers(CustomerDto customerDto) throws DuplicateCustomerException, ValidationException {
        validationContext.validate(customerDto);
        customerService.addCustomers(customerMapStruct.mapToCustomer(customerDto));

    }

    @Override
    public void updateCustomer(CustomerDto customerDto) throws ValidationException, CustomerNotFindException {
        validationContext.validate(customerDto);
        Customer customer = customerService.getCustomerById(customerDto.getId());
        customerMapStruct.mapToCustomer(customerDto, customer);
        customerService.updateCustomers(customer);

    }

    @Override
    public List<CustomerDto> getActiveCustomers() throws CustomerNotFindException, EmptyCustomerException {
        return customerMapStruct.mapCustomerDtoList(
                customerService.getActiveCustomers());
    }

    @Override
    public List<CustomerDto> getDeletedCustomers() throws CustomerNotFindException, EmptyCustomerException {
        return customerMapStruct.mapCustomerDtoList(
                customerService.getDeletedCustomers());
    }

    @Override
    public CustomerDto getCustomerById(Integer id) throws CustomerNotFindException {
        return customerMapStruct.mapToCustomerDto(
                customerService.getCustomerById(id));
    }

    @Override
    public Boolean login(String userName, String password) {
        return customerService.login(userName, password);
    }

    @Override
    public CustomerDto printCustomersByEmail(String email) throws CustomerNotFindException {
        return customerMapStruct.mapToCustomerDto(customerService.printCustomersByEmail(email));
    }

    @Override
    public void exportFileJson(String fileName) throws FileException {
        customerService.exportFileJson(fileName);
    }
}
