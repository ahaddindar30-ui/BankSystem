package com.mysite.banking.service;

import com.mysite.banking.model.Customer;
import com.mysite.banking.model.FileType;
import com.mysite.banking.service.exception.*;

import java.util.List;

public interface CustomerService {
    void deleteCustomerById(Integer id) throws CustomerNotFindException;

    List<Customer> printCustomerByFamily(String family);

    List<Customer> printCustomersByName(String name);

    void addCustomers(Customer customer) throws DuplicateCustomerException;

    List<Customer> getActiveCustomers() throws CustomerNotFindException, EmptyCustomerException;

    List<Customer> getDeletedCustomers() throws CustomerNotFindException, EmptyCustomerException;

    Customer getCustomerById(Integer id) throws CustomerNotFindException;

    void saveData(String name, FileType fileType) throws FileException;

    void loadData(String name, FileType fileType) throws FileException;

    void initData();

    void saveOnExit();

    void addData(String name) throws FileException;

    Boolean login(String userName, String password);

    Customer printCustomersByEmail(String email) throws CustomerNotFindException;

}
