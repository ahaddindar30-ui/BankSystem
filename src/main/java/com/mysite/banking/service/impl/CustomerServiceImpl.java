package com.mysite.banking.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.banking.dao.CustomerDao;
import com.mysite.banking.dao.impl.CustomerDaoImpl;
import com.mysite.banking.model.Customer;

import com.mysite.banking.service.CustomerService;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.util.MapperWrapper;
import com.mysite.banking.util.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao;
    private ObjectMapper objectMapper;


    private static final CustomerServiceImpl INSTANCE;

    public static CustomerServiceImpl getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new CustomerServiceImpl();
    }

    private CustomerServiceImpl() {
        this.customerDao = CustomerDaoImpl.getInstance();
        this.objectMapper = MapperWrapper.getInstance();


    }


    @Override
    public void deleteCustomerById(Integer id) throws CustomerNotFindException {
        Customer customerById = getCustomerById(id);
        customerDao.deleteCustomer(customerById);
    }


    @Override
    public List<Customer> printCustomerByFamily(String family) {
        return customerDao.getCustomerByFamily(family);


    }

    @Override
    public List<Customer> printCustomersByName(String name) {
        return customerDao.getCustomerByName(name);


    }


    @Override
    public void addCustomers(Customer customer) throws DuplicateCustomerException {
        Integer id = customerDao.saveCustomer(customer);
        customer.setPassword(PasswordEncoder.encoderPassword(customer.getPassword(), id));

        customerDao.updateCustomer(customer);

    }

    @Override
    public void updateCustomers(Customer customer) {
        customerDao.updateCustomer(customer);
    }


    @Override
    public List<Customer> getActiveCustomers() throws EmptyCustomerException {
        List<Customer> customerList = customerDao.getCustomerByStatus(false);
        if (customerList.isEmpty()) {
            throw new EmptyCustomerException();
        } else {
            return customerList;
        }

    }

    @Override
    public List<Customer> getDeletedCustomers() throws EmptyCustomerException {
        List<Customer> customerList = customerDao.getCustomerByStatus(true);
        if (customerList.isEmpty()) {
            throw new EmptyCustomerException();
        } else {
            return customerList;
        }
    }

    @Override
    public Customer getCustomerById(Integer id) throws CustomerNotFindException {
        Customer byId = customerDao.findCustomerById(id);
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
        Customer byEmail = customerDao.getCustomerByEmail(email);
        if (byEmail == null) {
            throw new CustomerNotFindException();
        } else {
            return byEmail;
        }
    }

    @Override
    public void exportFileJson(String fileName) throws FileException {
        File file = new File(fileName + ".json");
        try {
            List<Customer> customerList = customerDao.getAllCustomers(null);
            objectMapper.writeValue(file, customerList);
        } catch (IOException e) {
            throw new FileException();
        }
    }


}

