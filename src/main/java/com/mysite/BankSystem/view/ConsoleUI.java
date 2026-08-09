package com.mysite.BankSystem.view;

import com.mysite.BankSystem.dto.CustomerDto;
import com.mysite.BankSystem.facade.impl.CustomerFacadeImpl;
import com.mysite.BankSystem.model.CustomerType;
import com.mysite.BankSystem.model.FileType;
import com.mysite.BankSystem.service.exception.*;
import com.mysite.BankSystem.util.ScannerWrapper;
import com.mysite.BankSystem.view.component.AbstractCustomerUI;

import java.util.List;
import java.util.function.Function;

public class ConsoleUI implements AutoCloseable {
    private final CustomerFacadeImpl customerFacade;
    private final ScannerWrapper scannerWrapper;

    public ConsoleUI() {
        this.customerFacade = CustomerFacadeImpl.getInstance();
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
                    case 8:
                        saveData();
                        break;
                    case 9:
                        loadData();
                        break;
                    default:
                        System.out.println("Invalid Choice");
                }
            } catch (CustomerNotFindException | FileException | EmptyCustomerException ex) {
                System.out.println(ex.getMessage());
            }
        } while (choice != 0);
        scannerWrapper.close();


    }

    private void loadData() throws FileException {
        System.out.println("File type:");
        System.out.println("1.Serialaze");
        System.out.println("2.Jaon");
        int choice = scannerWrapper.getUserInput("Enter your choice: ", Integer::valueOf);

        try {
            FileType fileType = FileType.fromValue(choice);
            String name = scannerWrapper.getUserInput("Enter your file name: ", Function.identity());
            customerFacade.loadData(name,fileType);
        } catch (InvalidType ex) {
            System.out.println("Invalid Type exception.");
            loadData();
        }
    }

    private void saveData() throws FileException {
        System.out.println("File type:");
        System.out.println("1.Serialaze");
        System.out.println("2.Jaon");
        int choice = scannerWrapper.getUserInput("Enter your choice: ", Integer::valueOf);
        try {
            FileType fileType = FileType.fromValue(choice);
            String name = scannerWrapper.getUserInput("Enter your the name: ", Function.identity());
            customerFacade.saveData(name,fileType);
        } catch (InvalidType ex) {
            System.out.println("Invalid Type exception.");
            saveData();
        }
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
        System.out.println("7.Print all deleted customers");
        System.out.println("8.Save data");
        System.out.println("9.Load data");
        System.out.println();
    }


    public void addCustomers() {
        System.out.println("Customer type:");
        System.out.println("1.REAL");
        System.out.println("2.LEGAL");
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

    public void printAllDeletedCustomers() throws EmptyCustomerException, CustomerNotFindException {
        List<CustomerDto> allCustomer = customerFacade.getDeletedCustomers();
        System.out.println("All deleted Customers: ");
        for (CustomerDto customer : allCustomer) {
            System.out.println(customer);
        }
    }

    public void printAllCustomers() throws EmptyCustomerException, CustomerNotFindException {
        List<CustomerDto> allCustomer = customerFacade.getActiveCustomers();
        System.out.println("All Customers: ");
        for (CustomerDto customer : allCustomer) {
            System.out.println(customer);
        }
    }

    private void searchAndPrintCustomerByName() {
        String name = scannerWrapper.getUserInput("Enter your the name: ", Function.identity());
        List<CustomerDto> customers = customerFacade.printCustomersByName(name);
        customers.forEach(System.out::println);

    }

    private void searchAndPrintCustomerByFamily() {
        String family = scannerWrapper.getUserInput("Enter your the family: ", Function.identity());
        List<CustomerDto> customers = customerFacade.printCustomerByFamily(family);
        customers.forEach(System.out::println);

    }

    public void editCustomerById() throws CustomerNotFindException {
        String id = scannerWrapper.getUserInput("Enter your customer id: ", Function.identity());
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

    private void deletedCustomerById() throws CustomerNotFindException {
        String id = scannerWrapper.getUserInput("Enter your customer id: ", Function.identity());
        customerFacade.deleteCustomerById(Integer.valueOf(id));
    }


    @Override
    public void close() {
        scannerWrapper.close();
    }
}
