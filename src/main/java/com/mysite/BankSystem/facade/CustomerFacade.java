package com.mysite.BankSystem.facade;

import com.mysite.BankSystem.dto.CustomerDto;
import com.mysite.BankSystem.model.FileType;
import com.mysite.BankSystem.service.exception.*;

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
}
