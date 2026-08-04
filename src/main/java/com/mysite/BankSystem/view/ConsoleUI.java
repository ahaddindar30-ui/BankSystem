package com.mysite.BankSystem.view;

import com.mysite.BankSystem.model.Customer;
import com.mysite.BankSystem.model.CustomerType;
import com.mysite.BankSystem.service.CustomerService;
import com.mysite.BankSystem.util.ScannerWrapper;
import com.mysite.BankSystem.view.component.AbstractCustomerUI;

import java.util.List;
import java.util.function.Function;

public class ConsoleUI implements AutoCloseable {
    private final CustomerService customerService;
    private final ScannerWrapper scannerWrapper;

    public ConsoleUI() {
        this.customerService = CustomerService.getInstance();
        this.scannerWrapper = ScannerWrapper.getInstance();
    }

    public void startMenu() {
        int choice;
        do {
            printMenu();
            choice = scannerWrapper.getUserInput("Enter Choice: ",Integer::valueOf);
            switch (choice) {
                case 0:
                    System.out.println("Exit");
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
                default:
                    System.out.println("Invalid Choice");
            }
        } while (choice != 0);
        scannerWrapper.close();


    }


    public void printMenu() {
        System.out.println("Menu:");
        System.out.println("0.Exit");
        System.out.println("1.Add Customer");
        System.out.println("2.Print All Customers");
        System.out.println("3.Search and Customers By Name");
        System.out.println("4.Search and Customers By family");
        System.out.println("5.Edit Customers By id");
        System.out.println("6.Delete Customers By id");
        System.out.println("7.print all deleted customers");
        System.out.println();
    }


    public void addCustomers() {
        System.out.println("Customer type:");
        System.out.println("1.REAL");
        System.out.println("2.LEGAL");
        System.out.println();
        int choice = scannerWrapper.getUserInput("Enter your choice: ",Integer::valueOf);

        Customer customer = AbstractCustomerUI.fromCustomerUI(
                        CustomerType.fromValue(choice))
                .generateCustomerUI();
        customerService.addCustomers(customer);


    }

    public void printAllDeletedCustomers() {
        List<Customer> allCustomer = customerService.getDeletedCustomers();
        if (allCustomer.isEmpty()) {
            System.out.println("There is no  deleted customers.");
        } else {
            System.out.println("All deleted Customer: ");
            for (Customer customer : allCustomer) {
                System.out.println(customer);
            }
        }
    }

    public void printAllCustomers() {
        List<Customer> allCustomer = customerService.getActiveCustomers();
        if (allCustomer.isEmpty()) {
            System.out.println("There is no customer to add.");
        } else {
            System.out.println("All Customer: ");
            for (Customer customer : allCustomer) {
                System.out.println(customer);
            }
        }
    }

    private void searchAndPrintCustomerByName() {
        String name = scannerWrapper.getUserInput("Enter your the name: ", Function.identity());
        List<Customer> customers = customerService.printCustomersByName(name);
        customers.forEach(System.out::println);

    }

    private void searchAndPrintCustomerByFamily() {
        String family = scannerWrapper.getUserInput("Enter your the family: ", Function.identity());
        List<Customer> customers = customerService.printCustomerByFamily(family);
        customers.forEach(System.out::println);

    }

    public void editCustomerById() {
        String id = scannerWrapper.getUserInput("Enter your the id: ", Function.identity());
        Customer customer = customerService.editeCustomerById(Integer.valueOf(id));
        String name = scannerWrapper.getUserInput("Enter your new name: ", Function.identity());
        customer.setName(name);
        String number = scannerWrapper.getUserInput("Enter your new number: ", Function.identity());
        customer.setNumber(number);
        String email = scannerWrapper.getUserInput("Enter your new email: ", Function.identity());
        customer.setEmail(email);
        AbstractCustomerUI
                .fromCustomerUI(customer.getType())
                .editCustomer(customer);
    }

    private void deletedCustomerById() {
        String id = scannerWrapper.getUserInput("Enter your customer id: ", Function.identity());
        customerService.deleteCustomerById(Integer.valueOf(id));
    }


    @Override
    public void close() {
        scannerWrapper.close();
    }
}
