package com.mysite.BankSystem.view;

import com.mysite.BankSystem.model.Customer;
import com.mysite.BankSystem.model.CustomerType;
import com.mysite.BankSystem.service.exception.CustomerNotFindException;
import com.mysite.BankSystem.service.exception.DuplicateCustomerException;
import com.mysite.BankSystem.service.exception.EmptyCustomerException;
import com.mysite.BankSystem.service.exception.InvalidCustomerType;
import com.mysite.BankSystem.service.impl.CustomerServiceImpl;
import com.mysite.BankSystem.util.ScannerWrapper;
import com.mysite.BankSystem.view.component.AbstractCustomerUI;

import java.util.List;
import java.util.function.Function;

public class ConsoleUI implements AutoCloseable {
    private final CustomerServiceImpl customerServiceImpl;
    private final ScannerWrapper scannerWrapper;

    public ConsoleUI() {
        this.customerServiceImpl = CustomerServiceImpl.getInstance();
        this.scannerWrapper = ScannerWrapper.getInstance();
    }

    public void startMenu() {
        int choice;
        do {
            printMenu();
            choice = scannerWrapper.getUserInput("Enter Choice: ", Integer::valueOf);
            try {

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
            } catch (CustomerNotFindException | EmptyCustomerException ex) {
                System.out.println(ex.getMessage());
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
        int choice = scannerWrapper.getUserInput("Enter your choice: ", Integer::valueOf);
        try {
            customerServiceImpl.addCustomers(AbstractCustomerUI.fromCustomerUI(
                            CustomerType.fromValue(choice))
                    .generateCustomerUI());
        } catch (DuplicateCustomerException e) {
            System.out.println("it,s not possible to select duplicate email and national code and company registration.");
            addCustomers();
        } catch (InvalidCustomerType ex) {
            System.out.println("Invalid Customer Type exception.");
            addCustomers();
        }


    }

    public void printAllDeletedCustomers() throws EmptyCustomerException {
        List<Customer> allCustomer = customerServiceImpl.getDeletedCustomers();
        System.out.println("All deleted Customer: ");
        for (Customer customer : allCustomer) {
            System.out.println(customer);
        }
    }

    public void printAllCustomers() throws EmptyCustomerException {
        List<Customer> allCustomer = customerServiceImpl.getActiveCustomers();
        System.out.println("All Customer: ");
        for (Customer customer : allCustomer) {
            System.out.println(customer);
        }
    }

    private void searchAndPrintCustomerByName() {
        String name = scannerWrapper.getUserInput("Enter your the name: ", Function.identity());
        List<Customer> customers = customerServiceImpl.printCustomersByName(name);
        customers.forEach(System.out::println);

    }

    private void searchAndPrintCustomerByFamily() {
        String family = scannerWrapper.getUserInput("Enter your the family: ", Function.identity());
        List<Customer> customers = customerServiceImpl.printCustomerByFamily(family);
        customers.forEach(System.out::println);

    }

    public void editCustomerById() throws CustomerNotFindException {
        String id = scannerWrapper.getUserInput("Enter your the id: ", Function.identity());
        Customer customer = customerServiceImpl.editeCustomerById(Integer.valueOf(id));
        AbstractCustomerUI
                .fromCustomerUI(customer.getType())
                .editCustomer(customer);
    }

    private void deletedCustomerById() throws CustomerNotFindException {
        String id = scannerWrapper.getUserInput("Enter your customer id: ", Function.identity());
        customerServiceImpl.deleteCustomerById(Integer.valueOf(id));
    }


    @Override
    public void close() {
        scannerWrapper.close();
    }
}
