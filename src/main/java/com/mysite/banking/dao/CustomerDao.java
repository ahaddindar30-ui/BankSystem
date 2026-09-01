package com.mysite.banking.dao;

import com.mysite.banking.model.Customer;

import java.util.List;

public interface CustomerDao {
    Integer save(Customer customer);
    void update(Customer customer);
    void delete(Customer customer);
    Customer findById(Integer id);
    List<Customer> getByStatus(boolean deleted);
    List<Customer> getByName(String name);
    List<Customer> getByFamily(String family);
    Customer getByEmail(String email);
}
