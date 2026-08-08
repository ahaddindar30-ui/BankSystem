package com.mysite.BankSystem.service.impl;

import com.mysite.BankSystem.model.Customer;
import com.mysite.BankSystem.model.LegalCustomer;
import com.mysite.BankSystem.model.RealCustomer;
import com.mysite.BankSystem.service.CustomerService;
import com.mysite.BankSystem.service.exception.CustomerNotFindException;
import com.mysite.BankSystem.service.exception.DuplicateCustomerException;
import com.mysite.BankSystem.service.exception.EmptyCustomerException;
import com.mysite.BankSystem.service.exception.ValidationException;
import com.mysite.BankSystem.service.validation.ValidationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerServiceImpl implements CustomerService {
    private ArrayList<Customer> customers = new ArrayList<>();

    private ValidationContext<Customer> validationContext;

    private static final CustomerServiceImpl INSTANCE;

    public static CustomerServiceImpl getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new CustomerServiceImpl();
    }

    private CustomerServiceImpl() {
        this.validationContext = new CustomerValidationContext();

    }


    @Override
    public void deleteCustomerById(Integer id) throws CustomerNotFindException {
        editeCustomerById(id).setDeleted(true);
    }


    @Override
    public List<Customer> printCustomerByFamily(String family) {
        return customers.stream()
                .filter(customer -> !customer.isDeleted())
                .filter(customer -> customer instanceof RealCustomer)
                .map(customer -> (RealCustomer) customer)
                .filter(realCustomer -> realCustomer.getFamily().equalsIgnoreCase(family))
                .collect(Collectors.toList());


    }

    @Override
    public List<Customer> printCustomersByName(String name) {
        return customers.stream()
                .filter(customer -> !customer.isDeleted())
                .filter(customer -> customer.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());


    }


    @Override
    public void addCustomers(Customer customer) throws DuplicateCustomerException, ValidationException {
        Optional<Customer> any = customers.stream()
                .filter(it -> it.getEmail().equals(customer.getEmail()) &&
                        (((customer instanceof RealCustomer) &&
                                (it instanceof RealCustomer) &&
                                ((RealCustomer) it).getNationalCode().equals(((RealCustomer)
                                        customer).getNationalCode())) ||
                                ((customer instanceof LegalCustomer && it instanceof LegalCustomer) && ((LegalCustomer) it).getCompanyRegistration()
                                        .equals(((LegalCustomer) customer).getCompanyRegistration())))).findAny();
        if (any.isPresent()) {
            throw new DuplicateCustomerException();
        }

        validationContext.validate(customer);
        customers.add(customer);

    }


    @Override
    public List<Customer> getActiveCustomers() throws EmptyCustomerException {
        List<Customer> collect = customers.stream()
                .filter(customer -> !customer.isDeleted())
                .collect(Collectors.toList());
        if (collect.isEmpty()) {
            throw new EmptyCustomerException();
        }
        return collect;
    }

    @Override
    public List<Customer> getDeletedCustomers() throws EmptyCustomerException {
        List<Customer> collect = customers.stream()
                .filter(Customer::isDeleted)
                .collect(Collectors.toList());
        if (collect.isEmpty()) {
            throw new EmptyCustomerException();
        }
        return collect;
    }

    @Override
    public Customer editeCustomerById(Integer id) throws CustomerNotFindException {
        return customers.stream()
                .filter(customer -> !customer.isDeleted())
                .filter(customer -> customer.getId().equals(id))
                .findFirst().orElseThrow(CustomerNotFindException::new);
    }

}

