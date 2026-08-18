package com.mysite.banking.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.banking.dto.AccountDto;
import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.facade.AccountFacade;
import com.mysite.banking.facade.CustomerFacade;
import com.mysite.banking.facade.impl.AccountFacadeImpl;
import com.mysite.banking.facade.impl.CustomerFacadeImpl;
import com.mysite.banking.model.AccountType;
import com.mysite.banking.model.CustomerType;
import com.mysite.banking.model.FileType;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.util.ScannerWrapper;
import com.mysite.banking.view.component.AbstractCustomerUI;

import java.util.List;
import java.util.function.Function;

public class ConsoleUI implements AutoCloseable {
    private final AccountFacade accountFacade;
    private final CustomerFacade customerFacade;
    private final ScannerWrapper scannerWrapper;

    private final ObjectMapper objectMapper;

    public ConsoleUI() {
        this.customerFacade = CustomerFacadeImpl.getInstance();
        this.scannerWrapper = ScannerWrapper.getInstance();
        this.accountFacade = AccountFacadeImpl.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    private void saveOnExit() {
        customerFacade.saveOnExit();
        accountFacade.saveOnExit();
    }

    public void startMenu() {
        customerFacade.initData();
        accountFacade.initData();
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveOnExit));
        int choice;
        do {
            printMainMenu();
            choice = scannerWrapper.getUserInput("Enter Choice: ", Integer::valueOf);
            switch (choice) {
                case 0:
                    System.out.print("Exit");
                    break;
                case 1:
                    CustomerMenu();
                    break;
                case 2:
                    AccountMenu();
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        } while (choice != 0);
        scannerWrapper.close();


    }

    public void printMainMenu() {
        System.out.println("Menu:");
        System.out.println("0.Exit");
        System.out.println("1.Customer Management");
        System.out.println("2.Account Management");
        System.out.println();
    }

    public void CustomerMenu() {
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
                        saveData();
                        break;
                    case 9:
                        loadData();
                        break;
                    case 10:
                        addData();
                        break;
                    default:
                        System.out.println("Invalid Choice");
                }
            } catch (CustomerNotFindException | FileException | EmptyCustomerException ex) {
                System.out.println(ex.getMessage());
            }
        } while (choice != 0);

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
        System.out.println("8.Save data");
        System.out.println("9.Load data");
        System.out.println("10.Add data");
        System.out.println();
    }

    private void saveData() throws FileException {
        System.out.println("File type:");
        System.out.println("1.Serialaze");
        System.out.println("2.Jaon");
        System.out.println();
        int choice = scannerWrapper.getUserInput("Enter your choice: ", Integer::valueOf);
        try {
            FileType fileType = FileType.fromValue(choice);
            String name = scannerWrapper.getUserInput("Enter your file name: ", Function.identity());
            customerFacade.saveData(name, fileType);
        } catch (InvalidType ex) {
            System.out.println("Invalid Type exception.");
            saveData();
        }
    }

    private void loadData() throws FileException {
        System.out.println("File type:");
        System.out.println("1.Serialaze");
        System.out.println("2.Jaon");
        System.out.println();
        int choice = scannerWrapper.getUserInput("Enter your choice: ", Integer::valueOf);

        try {
            FileType fileType = FileType.fromValue(choice);
            String name = scannerWrapper.getUserInput("Enter your file name: ", Function.identity());
            customerFacade.loadData(name, fileType);
        } catch (InvalidType ex) {
            System.out.println("Invalid Type exception.");
            loadData();
        }
    }

    private void addData() throws FileException {
        String name = scannerWrapper.getUserInput("Enter your json file name: ", Function.identity());
        customerFacade.addData(name);

    }

    public void AccountMenu() {
        int choice;
        do {
            printAccountMenu();
            choice = scannerWrapper.getUserInput("Enter Choice: ", Integer::valueOf);
            try {
                switch (choice) {
                    case 0:
                        break;
                    case 1:
                        addAccounts();
                        break;
                    case 2:
                        printAllAccounts();
                        break;
                    case 3:
                        searchAndPrintAccountByCustomerName();
                        break;
                    case 4:
                        editAccountById();
                        break;
                    case 5:
                        deletedAccountById();
                        break;
                    case 6:
                        printAllDeletedAccounts();
                        break;
                    case 7:
                        deposit();
                        break;
                    case 8:
                        withdraw();
                        break;
                    case 9:
                        saveAccountData();
                        break;
                    case 10:
                        loadAccountData();
                        break;
                    case 11:
                        addAccountData();
                        break;
                    default:
                        System.out.println("Invalid Choice");
                }
            } catch (AccountNotFindException | FileException | EmptyAccountException | ValidationException ex) {
                System.out.println(ex.getMessage());
            }
        } while (choice != 0);
    }

    private void withdraw() throws AccountNotFindException, ValidationException {
        int accountId = scannerWrapper.getUserInput("Enter the account id ", Integer::valueOf);
        Double amount = scannerWrapper.getUserInput("Enter the amount ", Double::valueOf);
        accountFacade.withdraw(accountId, amount);

    }

    public void printAccountMenu() {
        System.out.println("Menu:");
        System.out.println("0.Back");
        System.out.println("1.Add account");
        System.out.println("2.Print all accounts");
        System.out.println("3.search and print customer by name");
        System.out.println("4.Edit account by id");
        System.out.println("5.Delete accounts by id");
        System.out.println("6.Print all deleted accounts");
        System.out.println("7.Deposit");
        System.out.println("8.Withdraw");
        System.out.println("9.Save data");
        System.out.println("10.Load data");
        System.out.println("11.Add data");
        System.out.println();
    }

    private void deposit() throws AccountNotFindException {
        int accountId = scannerWrapper.getUserInput("Enter the account id ", Integer::valueOf);
        Double amount = scannerWrapper.getUserInput("Enter the amount ", Double::valueOf);
        accountFacade.deposit(accountId, amount);
    }

    private void addAccountData() throws FileException {
        String name = scannerWrapper.getUserInput("Enter your json file name: ", Function.identity());
        accountFacade.addData(name);

    }

    private void loadAccountData() throws FileException {
        System.out.println("File type:");
        System.out.println("1.Serialaze");
        System.out.println("2.Jaon");
        System.out.println();
        int choice = scannerWrapper.getUserInput("Enter your choice: ", Integer::valueOf);
        try {
            FileType fileType = FileType.fromValue(choice);
            String name = scannerWrapper.getUserInput("Enter your file name: ", Function.identity());
            accountFacade.loadData(name, fileType);
        } catch (InvalidType ex) {
            System.out.println("Invalid Type exception.");
            loadAccountData();
        }
    }

    private void saveAccountData() throws FileException {
        System.out.println("File type:");
        System.out.println("1.Serialaze");
        System.out.println("2.Jaon");
        System.out.println();
        int choice = scannerWrapper.getUserInput("Enter your choice: ", Integer::valueOf);
        try {
            FileType fileType = FileType.fromValue(choice);
            String name = scannerWrapper.getUserInput("Enter your file name: ", Function.identity());
            accountFacade.saveData(name, fileType);
        } catch (InvalidType ex) {
            System.out.println("Invalid Type exception.");
            saveAccountData();
        }
    }


    private void searchAndPrintAccountByCustomerName() {
        String name = scannerWrapper.getUserInput("Enter the name: ", Function.identity());
        List<AccountDto> accountDtoList = accountFacade.printAccountByCustomerName(name);
        accountDtoList.forEach(accountDto -> {
            try {
                System.out.println(objectMapper.writeValueAsString(accountDto));
            } catch (JsonProcessingException e) {
                System.out.println("Error on print account id " + accountDto.getId());
            }
        });

    }

    public void addAccounts() {
        System.out.println("Account type:");
        System.out.println("1.EURO");
        System.out.println("2.DOLLAR");
        System.out.println();
        int choice = scannerWrapper.getUserInput("Enter your choice: ", Integer::valueOf);
        try {
            AccountType type = AccountType.fromValue(choice);
            int number = scannerWrapper.getUserInput("Enter Customer id : ", Integer::valueOf);
            AccountDto accountDto = new AccountDto(null, type, 0.0, number);
            accountFacade.addAccounts(accountDto);
        } catch (InvalidType ex) {
            System.out.println("Invalid Account Type exception.");
            addAccounts();
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            addAccounts();
        }


    }

    public void printAllDeletedAccounts() throws EmptyAccountException, AccountNotFindException {
        List<AccountDto> allAccount = accountFacade.getDeletedAccounts();
        System.out.println("All deleted Accounts: ");
        for (AccountDto account : allAccount) {
            try {
                System.out.println(objectMapper.writeValueAsString(account));
            } catch (JsonProcessingException e) {
                System.out.println("Error on print account ");
            }
        }
    }

    public void printAllAccounts() throws EmptyAccountException, AccountNotFindException {
        List<AccountDto> allAccount = accountFacade.getActiveAccounts();
        System.out.println("All Customers: ");
        for (AccountDto account : allAccount) {
            try {
                System.out.println(objectMapper.writeValueAsString(account));
            } catch (JsonProcessingException e) {
                System.out.println("Error on print account id " + account.getId());

            }
        }
    }

    public void editAccountById() throws AccountNotFindException {
        String id = scannerWrapper.getUserInput("Enter the account id: ", Function.identity());
        AccountDto accountDto = accountFacade.getAccountById(Integer.valueOf(id));
        int number = scannerWrapper.getUserInput("Enter new Customer id : ", Integer::valueOf);
        accountDto.setCustomerId(number);
        try {
            accountFacade.updateAccount(accountDto);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            editAccountById();
        }
    }

    private void deletedAccountById() throws AccountNotFindException {
        String id = scannerWrapper.getUserInput("Enter account id: ", Function.identity());
        accountFacade.deleteAccountById(Integer.valueOf(id));
    }

    @Override
    public void close() {
        scannerWrapper.close();
    }
}
