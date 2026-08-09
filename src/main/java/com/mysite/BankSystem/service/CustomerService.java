package com.mysite.BankSystem.service;

import com.mysite.BankSystem.model.Customer;
import com.mysite.BankSystem.model.FileType;
import com.mysite.BankSystem.service.exception.*;

import java.util.List;

public interface CustomerService {
    void deleteCustomerById(Integer id) throws CustomerNotFindException;

    List<Customer> printCustomerByFamily(String family);

    List<Customer> printCustomersByName(String name);

    void addCustomers(Customer customer) throws DuplicateCustomerException, ValidationException;
    void updateCustomer(Customer customer) throws ValidationException;

    List<Customer> getActiveCustomers() throws CustomerNotFindException, EmptyCustomerException;

    List<Customer> getDeletedCustomers() throws CustomerNotFindException, EmptyCustomerException;

    Customer getCustomerById(Integer id) throws CustomerNotFindException;

    void saveData(String name, FileType fileType) throws FileException;

    void loadData(String name, FileType fileType) throws FileException;

    void initData();

    void saveOnExit();

    void addData(String name) throws FileException;
}
