package com.mysite.BankSystem.service;

import com.mysite.BankSystem.model.Customer;
import com.mysite.BankSystem.model.RealCustomer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerService {
    private  ArrayList<Customer> customers = new ArrayList<>();

    private static final CustomerService INSTANCE;
    public static CustomerService getInstance() {
        return INSTANCE;
    }
    static {
        INSTANCE = new CustomerService();
    }
    private CustomerService() {

    }




    public void deleteCustomerById(Integer id) {
        customers.stream()
                .filter(customer -> !customer.isDeleted())
                .filter(customer -> customer.getId().equals(id))
                .forEach(customer -> customer.setDeleted(true));
    }



    public List<Customer> printCustomerByFamily(String  family) {
       return customers.stream()
               .filter(customer -> !customer.isDeleted())
               .filter(customer -> customer instanceof RealCustomer)
               .map(customer -> (RealCustomer) (customer))
               .filter(realCustomer -> realCustomer.getFamily().equalsIgnoreCase(family))
               .collect(Collectors.toList());


    }

    public List<Customer> printCustomersByName(String name) {
        return customers.stream()
                .filter(customer -> !customer.isDeleted())
                .filter(customer -> customer.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());


    }



    public void addCustomers(Customer customer) {
        customers.add(customer);

    }



    public List<Customer> getActiveCustomers() {
        return customers.stream()
                 .filter(customer -> !customer.isDeleted())
                 .collect(Collectors.toList());
    }

    public List<Customer> getDeletedCustomers() {
        return customers.stream()
                 .filter(Customer::isDeleted)
                .collect(Collectors.toList());
    }

    public Customer editeCustomerById(Integer id) {
        return customers.stream()
                .filter(customer -> !customer.isDeleted())
                .filter(customer -> customer.getId().equals(id))
                .findFirst().get();
    }

}

