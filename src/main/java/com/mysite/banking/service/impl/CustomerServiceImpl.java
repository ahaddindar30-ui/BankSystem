package com.mysite.banking.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.banking.model.Customer;
import com.mysite.banking.model.FileType;
import com.mysite.banking.model.RealCustomer;
import com.mysite.banking.service.CustomerService;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.util.MapperWrapper;
import com.mysite.banking.util.PasswordEncoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CustomerServiceImpl implements CustomerService {
    private static AtomicInteger ID_COUNTER = new AtomicInteger(1);
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
    public void addCustomers(Customer customer) throws DuplicateCustomerException {
        List<Customer> collect = customers.stream()
                .filter(it -> it.equals(customer))
                .findAny()
                .stream()
                .toList();

        if (!collect.isEmpty()) {
            throw new DuplicateCustomerException();
        }
        customer.setId(ID_COUNTER.getAndIncrement());
        customer.setPassword(PasswordEncoder.encoderPassword(customer.getPassword(), customer.getId()));
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

        updateIdCounter();

    }

    private void updateIdCounter() {
        Customer maxIdCustomer = customers.stream()
                .max((customer1, customer2) ->
                        Integer.compare(customer1.getId(), customer2.getId()))
                .orElse(null);
        if (maxIdCustomer == null) {
            ID_COUNTER = new AtomicInteger(1);
        } else {
            ID_COUNTER = new AtomicInteger(maxIdCustomer.getId() +1);
        }
    }

    @Override
    public void initData() {
        try {
            loadJson("init Customer Data");
            updateIdCounter();
        } catch (FileException ignored) {

        }
    }

    @Override
    public void saveOnExit() {
        try {
            saveJson("init Customer Data");
        } catch (FileException ignored) {

        }
    }

    @Override
    public void addData(String name) throws FileException {
        try {
            ArrayList<Customer> newCustomers = objectMapper.readValue(new File(name + ".json"),
                    new TypeReference<ArrayList<Customer>>() {
                    });
            customers.addAll(newCustomers);
        } catch (IOException e) {
            throw new FileException();
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
        return customers.stream()
                .filter(customer -> !customer.isDeleted())
                .filter(customer -> customer.getEmail().equalsIgnoreCase(email))
                .findFirst().orElseThrow(CustomerNotFindException::new);
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

