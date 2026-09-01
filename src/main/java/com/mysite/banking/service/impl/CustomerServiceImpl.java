package com.mysite.banking.service.impl;


import com.mysite.banking.dao.CustomerDao;
import com.mysite.banking.dao.impl.CustomerDaoImpl;
import com.mysite.banking.model.Customer;

import com.mysite.banking.service.CustomerService;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.util.PasswordEncoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao;


    private static final CustomerServiceImpl INSTANCE;

    public static CustomerServiceImpl getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new CustomerServiceImpl();
    }

    private CustomerServiceImpl() {
        this.customerDao = CustomerDaoImpl.getInstance();


    }


    @Override
    public void deleteCustomerById(Integer id) throws CustomerNotFindException {
        Customer customerById = getCustomerById(id);
        customerDao.delete(customerById);
    }


    @Override
    public List<Customer> printCustomerByFamily(String family) {
        return customerDao.getByFamily(family);


    }

    @Override
    public List<Customer> printCustomersByName(String name) {
        return customerDao.getByName(name);


    }


    @Override
    public void addCustomers(Customer customer) throws DuplicateCustomerException {
        Integer id = customerDao.save(customer);
        customer.setPassword(PasswordEncoder.encoderPassword(customer.getPassword(), id));

        customerDao.update(customer);

    }

    @Override
    public void updateCustomers(Customer customer) {
        customerDao.update(customer);
    }


    @Override
    public List<Customer> getActiveCustomers() throws EmptyCustomerException {
        List<Customer> customerList = customerDao.getByStatus(false);
        if (customerList.isEmpty()) {
            throw new EmptyCustomerException();
        } else {
            return customerList;
        }

    }

    @Override
    public List<Customer> getDeletedCustomers() throws EmptyCustomerException {
        List<Customer> customerList = customerDao.getByStatus(true);
        if (customerList.isEmpty()) {
            throw new EmptyCustomerException();
        } else {
            return customerList;
        }
    }

    @Override
    public Customer getCustomerById(Integer id) throws CustomerNotFindException {
        Customer byId = customerDao.findById(id);
        if (byId == null) {
            throw new CustomerNotFindException();
        } else {
            return byId;
        }
    }


    @Override
    public Boolean login(String userName, String password) {
        try {
            Customer customer = printCustomersByEmail(userName);
            return Objects.equals(
                    customer.getPassword(),
                    PasswordEncoder.encoderPassword(password, customer.getId()));
        } catch (CustomerNotFindException e) {
            return false;
        }

    }

    @Override
    public Customer printCustomersByEmail(String email) throws CustomerNotFindException {
        Customer byEmail = customerDao.getByEmail(email);
        if (byEmail == null) {
            throw new CustomerNotFindException();
        } else {
            return byEmail;
        }
    }


}

