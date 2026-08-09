package com.mysite.BankSystem.facade.impl;

import com.mysite.BankSystem.dto.CustomerDto;
import com.mysite.BankSystem.facade.CustomerFacade;
import com.mysite.BankSystem.mapper.CustomerMapper;
import com.mysite.BankSystem.model.Customer;
import com.mysite.BankSystem.model.FileType;
import com.mysite.BankSystem.service.CustomerService;
import com.mysite.BankSystem.service.exception.*;
import com.mysite.BankSystem.service.impl.CustomerServiceImpl;
import com.mysite.BankSystem.service.impl.CustomerValidationContext;
import com.mysite.BankSystem.service.validation.ValidationContext;

import java.util.List;

public class CustomerFacadeImpl implements CustomerFacade {


    private ValidationContext<CustomerDto> validationContext;

    private final CustomerService customerService;


    private static final CustomerFacadeImpl INSTANCE;

    public static CustomerFacadeImpl getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new CustomerFacadeImpl();
    }

    private CustomerFacadeImpl() {
        this.customerService = CustomerServiceImpl.getInstance();
        this.validationContext = new CustomerValidationContext();
    }




    @Override
    public void deleteCustomerById(Integer id) throws CustomerNotFindException {
        customerService.deleteCustomerById(id);
    }

    @Override
    public List<CustomerDto> printCustomerByFamily(String family) {
        return CustomerMapper.mapCustomerDtoList(
                customerService.printCustomerByFamily(family));
    }

    @Override
    public List<CustomerDto> printCustomersByName(String name) {
        return CustomerMapper.mapCustomerDtoList(
                customerService.printCustomersByName(name));
    }

    @Override
    public void addCustomers(CustomerDto customerDto) throws DuplicateCustomerException, ValidationException {
    validationContext.validate(customerDto);
    customerService.addCustomers(CustomerMapper.mapToCustomer(customerDto));

    }

    @Override
    public void updateCustomer(CustomerDto customerDto) throws ValidationException, CustomerNotFindException {
        validationContext.validate(customerDto);
        Customer customer = customerService.getCustomerById(customerDto.getId());
        customerService.updateCustomer(CustomerMapper.mapToCustomer(customerDto, customer));

    }

    @Override
    public List<CustomerDto> getActiveCustomers() throws CustomerNotFindException, EmptyCustomerException {
        return CustomerMapper.mapCustomerDtoList(
                customerService.getActiveCustomers());
    }

    @Override
    public List<CustomerDto> getDeletedCustomers() throws CustomerNotFindException, EmptyCustomerException {
        return CustomerMapper.mapCustomerDtoList(
                customerService.getDeletedCustomers());
    }

    @Override
    public CustomerDto getCustomerById(Integer id) throws CustomerNotFindException {
        return CustomerMapper.mapToCustomerDto(
                customerService.getCustomerById(id));
    }

    public void saveData(String name, FileType fileType) throws FileException {
        customerService.saveData(name ,fileType);
    }

    @Override
    public void loadData(String name, FileType fileType) throws FileException {
        customerService.loadData(name ,fileType);
    }

    @Override
    public void initData() {
        customerService.initData();
    }

    @Override
    public void saveOnExit() {
        customerService.saveOnExit();
    }

    @Override
    public void addData(String name) throws FileException {
        customerService.addData(name);
    }
}
