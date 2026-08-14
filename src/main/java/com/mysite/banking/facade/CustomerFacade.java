package com.mysite.banking.facade;

import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.model.FileType;
import com.mysite.banking.service.exception.*;

import java.util.List;

public interface CustomerFacade {

    void deleteCustomerById(Integer id) throws CustomerNotFindException;

    List<CustomerDto> printCustomerByFamily(String family);

    List<CustomerDto> printCustomersByName(String name);

    void addCustomers(CustomerDto customer) throws DuplicateCustomerException, ValidationException;
    void updateCustomer(CustomerDto customer) throws ValidationException, CustomerNotFindException;

    List<CustomerDto> getActiveCustomers() throws CustomerNotFindException, EmptyCustomerException;

    List<CustomerDto> getDeletedCustomers() throws CustomerNotFindException, EmptyCustomerException;

    CustomerDto getCustomerById(Integer id) throws CustomerNotFindException;
    void saveData(String name, FileType fileType) throws FileException;

    void loadData(String name, FileType fileType) throws FileException;

    void initData();

    void saveOnExit();

    void addData(String name) throws FileException;
}
