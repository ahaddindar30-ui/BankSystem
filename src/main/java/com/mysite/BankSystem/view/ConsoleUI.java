package com.mysite.BankSystem.view;

import com.mysite.BankSystem.model.Customer;
import com.mysite.BankSystem.model.LegalCustomer;
import com.mysite.BankSystem.model.RealCustomer;
import com.mysite.BankSystem.service.CustomerService;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI implements AutoCloseable {
    private final CustomerService customerService = new CustomerService();
    private final Scanner scanner = new Scanner(System.in);


    public void startMenu() {
        int choice;
        do {
            printMenu();
            System.out.print("Enter Choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
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
                    searchAndEditCustomerByName();
                    break;
                case 6:
                    searchAndDeleteCustomerByName();
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        } while (choice != 0);
        scanner.close();


    }


    public void printMenu() {
        System.out.println("Menu:");
        System.out.println("0.Exit");
        System.out.println("1.Add Customer");
        System.out.println("2.Print All Customers");
        System.out.println("3.Search and print Customers By Name");
        System.out.println("4.Search and print Customers By family");
        System.out.println("5.Search and edit Customers By name");
        System.out.println("6.Search and deleted Customers By name");
        System.out.println();
    }


    private String getUserInput(String message) {
        System.out.println(message);
        return scanner.nextLine();

    }

    public void addCustomers() {
        System.out.println("Customer type:");
        System.out.println("1.REAL");
        System.out.println("2.LEGAL");
        System.out.println();
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice == 1) {
            String name = getUserInput("Enter your name: ");
            String number = getUserInput("Enter your number: ");
            String email = getUserInput("Enter your email: ");
            String family = getUserInput("Enter your family: ");
            String nationalCode = getUserInput("Enter your nationalCode: ");
            RealCustomer realCustomer = new RealCustomer(name, number, email);
            realCustomer.setFamily(family);
            realCustomer.setNationalCode(nationalCode);
            customerService.addCustomers(realCustomer);
        } else if (choice == 2) {
            String name = getUserInput("Enter your name: ");
            String number = getUserInput("Enter your number: ");
            String email = getUserInput("Enter your email: ");
            String fax = getUserInput("Enter your fax number: ");
            String companyRegistration = getUserInput("Enter your company Registration: ");
            LegalCustomer legalCustomer = new LegalCustomer(name, number, email);
            legalCustomer.setFaxNumber(fax);
            legalCustomer.setCompanyRegistration(companyRegistration);
            customerService.addCustomers(legalCustomer);
        }


    }

    public void printAllCustomers() {
        List<Customer> allCustomer = customerService.getAllCustomers();
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
        String name = getUserInput("Enter your the name: ");
        List<Customer> customers = customerService.searchCustomersByName(name);
        customers.forEach(System.out::println);

    }

    private void searchAndPrintCustomerByFamily() {
        String family = getUserInput("Enter your the family: ");
        List<Customer> customers = customerService.printCustomerByFamily(family);
        customers.forEach(System.out::println);

    }

    public void searchAndEditCustomerByName() {
        String name = getUserInput("Enter your the name: ");
        List<Customer> customers = customerService.searchCustomersByName(name);
        for (Customer customer : customers) {
            if (customer.getName().equals(name)) {
                String theName = getUserInput("Enter your the name: ");
                customer.setName(theName);
                String number = getUserInput("Enter your the number: ");
                customer.setNumber(number);
                String email = getUserInput("Enter your the email: ");
                customer.setEmail(email);
                if (customer instanceof RealCustomer realCustomer) {
                    String family = getUserInput("Enter your the family: ");
                    realCustomer.setFamily(family);
                    String nationalCode = getUserInput("Enter your the nationalCode: ");
                    realCustomer.setNationalCode(nationalCode);
                } else if (customer instanceof LegalCustomer legalCustomer) {
                    String fax = getUserInput("Enter your the fax number: ");
                    legalCustomer.setFaxNumber(fax);
                    String companyRegistration = getUserInput("Enter your the company Registration: ");
                    legalCustomer.setCompanyRegistration(companyRegistration);
                }
            }
        }

    }

    private void searchAndDeleteCustomerByName() {
        String name = getUserInput("Enter your the name: ");
        customerService.searchDeleteCustomerByName(name);
    }


    @Override
    public void close() {
        scanner.close();
    }
}
