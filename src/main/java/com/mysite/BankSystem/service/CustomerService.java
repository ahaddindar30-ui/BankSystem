package com.mysite.BankSystem.service;

import com.mysite.BankSystem.model.Customer;
import com.mysite.BankSystem.model.LegalCustomer;
import com.mysite.BankSystem.model.RealCustomer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerService {
    private final ArrayList<Customer> customers = new ArrayList<>();




    public void searchDeleteCustomerByName(String name) {
        customers.removeIf(customer -> customer.getName().equals(name));
    }



    public List<Customer> printCustomerByFamily(String  family) {
       return customers.stream()
               .filter(customer -> customer instanceof RealCustomer)
               .map(customer -> (RealCustomer) (customer))
               .filter(realCustomer -> realCustomer.getFamily().equalsIgnoreCase(family))
               .collect(Collectors.toList());


    }

    public List<Customer> searchCustomersByName(String name) {
        return customers.stream()
                .filter(customer -> customer.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());


    }



    public void addCustomers(Customer customer) {
        customers.add(customer);

    }



    public List<Customer> getAllCustomers() {
        return customers;
    }

}

