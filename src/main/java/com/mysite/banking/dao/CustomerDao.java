package com.mysite.banking.dao;


import com.mysite.banking.model.Customer;

import java.util.List;

public interface CustomerDao {
    Integer saveCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomer(Customer customer);

    Customer findCustomerById(Integer id);

    List<Customer> getCustomerByStatus(Boolean deleted);

    List<Customer> getAllCustomers(Boolean deleted);

    List<Customer> getCustomerByName(String name);

    List<Customer> getCustomerByFamily(String family);

    Customer getCustomerByEmail(String email);
}
