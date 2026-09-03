package com.mysite.banking.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.facade.CustomerFacade;
import com.mysite.banking.facade.impl.CustomerFacadeImpl;
import com.mysite.banking.model.CustomerType;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.view.component.AbstractCustomerUI;

import java.util.List;
import java.util.function.Function;

public class CustomerConsole extends BaseConsole {
    protected final CustomerFacade customerFacade;

    public CustomerConsole() {
        this.customerFacade = CustomerFacadeImpl.getInstance();
    }

    public void printCustomerMenu() {
        System.out.println("Menu:");
        System.out.println("0.Back");
        System.out.println("1.Add Customer");
        System.out.println("2.Print All Customers");
        System.out.println("3.Search and Customers By Name");
        System.out.println("4.Search and Customers By family");
        System.out.println("5.Edit Customer By id");
        System.out.println("6.Delete Customers By id");
        System.out.println("7.Print all deleted customers");
        System.out.println("8.Export JSON");
        System.out.println("9.Login");
        System.out.println();
    }

    public void menu() {
        int choice;
        do {
            printCustomerMenu();
            choice = scannerWrapper.getUserInput("Enter Choice: ", Integer::valueOf);
            try {
                switch (choice) {
                    case 0:
                        break;
                    case 1:
                        addCustomers();
                        break;
                    case 2:
                        printAllCustomers();
                        break;
                    case 3:
                        searchAndPrintCustomerByName();
                        break;
                    case 4:
                        searchAndPrintCustomerByFamily();
                        break;
                    case 5:
                        editCustomerById();
                        break;
                    case 6:
                        deletedCustomerById();
                        break;
                    case 7:
                        printAllDeletedCustomers();
                        break;
                    case 8:
                        exportFileJson();
                        break;
                    case 9:
                        login();
                        break;
                    default:
                        System.out.println("Invalid Choice");
                }
            } catch (CustomerNotFindException | EmptyCustomerException | FileException ex) {
                System.out.println(ex.getMessage());
            }
        } while (choice != 0);

    }

    private void exportFileJson() throws FileException {
        String fileName = scannerWrapper.getUserInput("Enter file name: ",Function.identity());
        customerFacade.exportFileJson(fileName);

    }


    private void login() {
        String userName = scannerWrapper.getUserInput("Enter your email: ", Function.identity());
        String password = scannerWrapper.getUserInput("Enter your password: ", Function.identity());
        Boolean validate = customerFacade.login(userName, password);
        if (validate) {
            System.out.println("Welcome to the system.");
        } else {
            System.out.println("username or password is wrong!");
        }

    }

    private void deletedCustomerById() throws CustomerNotFindException {
        String id = scannerWrapper.getUserInput("Enter customer id: ", Function.identity());
        customerFacade.deleteCustomerById(Integer.valueOf(id));
    }

    public void editCustomerById() throws CustomerNotFindException {
        String id = scannerWrapper.getUserInput("Enter the customer id: ", Function.identity());
        CustomerDto customerDto = customerFacade.getCustomerById(Integer.valueOf(id));
        AbstractCustomerUI
                .fromCustomerUI(customerDto.getType())
                .editCustomer(customerDto);
        try {
            customerFacade.updateCustomer(customerDto);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            editCustomerById();
        }
    }

    private void searchAndPrintCustomerByName() {
        String name = scannerWrapper.getUserInput("Enter the name: ", Function.identity());
        List<CustomerDto> customers = customerFacade.printCustomersByName(name);
        customers.forEach(customer -> {
            try {
                System.out.println(objectMapper.writeValueAsString(customer));
            } catch (JsonProcessingException e) {
                System.out.println("Error on print customer id " + customer.getId());
            }
        });

    }

    private void searchAndPrintCustomerByFamily() {
        String family = scannerWrapper.getUserInput("Enter the family: ", Function.identity());
        List<CustomerDto> customers = customerFacade.printCustomerByFamily(family);
        customers.forEach(customer -> {
            try {
                System.out.println(objectMapper.writeValueAsString(customer));
            } catch (JsonProcessingException e) {
                System.out.println("Error on print customer id " + customer.getId());
            }
        });

    }

    public void printAllCustomers() throws EmptyCustomerException, CustomerNotFindException {
        List<CustomerDto> allCustomer = customerFacade.getActiveCustomers();
        System.out.println("All Customers: ");
        for (CustomerDto customer : allCustomer) {
            try {
                System.out.println(objectMapper.writeValueAsString(customer));
            } catch (JsonProcessingException e) {
                System.out.println("Error on print customer id " + customer.getId());

            }
        }
    }

    public void printAllDeletedCustomers() throws EmptyCustomerException, CustomerNotFindException {
        List<CustomerDto> allCustomer = customerFacade.getDeletedCustomers();
        System.out.println("All deleted Customers: ");
        for (CustomerDto customer : allCustomer) {
            try {
                System.out.println(objectMapper.writeValueAsString(customer));
            } catch (JsonProcessingException e) {
                System.out.println("Error on print customer ");
            }
        }
    }

    public void addCustomers() {
        System.out.println("Customer type:");
        System.out.println("1.REAL");
        System.out.println("2.LEGAL");
        System.out.println();
        int choice = scannerWrapper.getUserInput("Enter your choice: ", Integer::valueOf);
        try {
            customerFacade.addCustomers(AbstractCustomerUI.fromCustomerUI(
                            CustomerType.fromValue(choice))
                    .generateCustomerUI());
        } catch (DuplicateCustomerException e) {
            System.out.println("it,s not possible to select duplicate email and national code and company registration.");
            addCustomers();
        } catch (InvalidType ex) {
            System.out.println("Invalid Customer Type exception.");
            addCustomers();
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            addCustomers();
        }


    }
}
