package com.mysite.banking.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.banking.model.Customer;
import com.mysite.banking.model.FileType;
import com.mysite.banking.model.LegalCustomer;
import com.mysite.banking.model.RealCustomer;
import com.mysite.banking.service.CustomerService;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.util.MapperWrapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerServiceImpl implements CustomerService {
    private ArrayList<Customer> customers;
    private final ObjectMapper objectMapper;


    private static final CustomerServiceImpl INSTANCE;

    public static CustomerServiceImpl getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new CustomerServiceImpl();
    }

    private CustomerServiceImpl() {
        this.customers = new ArrayList<>();
        this.objectMapper = MapperWrapper.getInstance();


    }


    @Override
    public void deleteCustomerById(Integer id) throws CustomerNotFindException {
        getCustomerById(id).setDeleted(true);
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


        customers.add(customer);

    }

    @Override
    public void updateCustomer(Customer customer) throws ValidationException {

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
    public Customer getCustomerById(Integer id) throws CustomerNotFindException {
        return customers.stream()
                .filter(customer -> !customer.isDeleted())
                .filter(customer -> customer.getId().equals(id))
                .findFirst().orElseThrow(CustomerNotFindException::new);
    }

    @Override
    public void saveData(String name, FileType fileType) throws FileException {
        switch (fileType) {
            case FileType.JSON -> saveJson(name);
            case FileType.SERIALIZE -> saveSerialize(name);
        }


    }

    private void saveJson(String name) throws FileException {
        try {
            File file = new File(name + ".json");
            if (!file.exists()) {
                file.createNewFile();
            }
            objectMapper.writeValue(file, customers);
        } catch (IOException e) {
            throw new FileException();
        }

    }

    private void saveSerialize(String name) throws FileException {
        try {
            File file = new File(name + ".crm");
            if (!file.exists()) {
                file.createNewFile();
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                objectOutputStream.writeObject(customers);
            }
        } catch (IOException e) {
            throw new FileException();
        }
    }

    @Override
    public void loadData(String name, FileType fileType) throws FileException {
        switch (fileType) {
            case FileType.JSON -> loadJson(name);
            case FileType.SERIALIZE -> loadSerialize(name);
        }

    }

    @Override
    public void initData() {
        try {
            loadJson("initData");
        } catch (FileException ignored) {

        }
    }

    @Override
    public void saveOnExit() {
        try {
            saveJson("initData");
        } catch (FileException ignored) {

        }
    }

    @Override
    public void addData(String name) throws FileException {
        try {
            ArrayList<Customer> newCustomers  = objectMapper.readValue(new File(name + ".json"),
                    new TypeReference<ArrayList<Customer>>() {});
            customers.addAll(newCustomers);
        } catch (IOException e) {
            throw new FileException();
        }
    }

    private void loadSerialize(String name) throws FileException {
        try (FileInputStream fileInputStream = new FileInputStream(name + ".crm");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            customers = (ArrayList<Customer>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new FileException();
        }
    }


    private void loadJson(String name) throws FileException {
        try {
            customers = objectMapper.readValue(new File(name + ".json"),
                    new TypeReference<ArrayList<Customer>>() {
                    });
        } catch (IOException e) {
            throw new FileException();
        }
    }

}

