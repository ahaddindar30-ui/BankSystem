package com.mysite.banking.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysite.banking.dto.AccountDto;
import com.mysite.banking.dto.AmountDto;
import com.mysite.banking.facade.AccountFacade;
import com.mysite.banking.facade.impl.AccountFacadeImpl;
import com.mysite.banking.model.FileType;
import com.mysite.banking.service.exception.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.function.Function;

public class AccountConsole extends BaseConsole {
    private final AccountFacade accountFacade;

    public AccountConsole() {
        this.accountFacade = AccountFacadeImpl.getInstance();
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
        System.out.println("9.Transfer");
        System.out.println("10.Save data");
        System.out.println("11.Load data");
        System.out.println("12.Add data");
        System.out.println();
    }

    public void menu() {
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
                        transfer();
                        break;
                    case 10:
                        saveAccountData();
                        break;
                    case 11:
                        loadAccountData();
                        break;
                    case 12:
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

    private void transfer() throws AccountNotFindException, ValidationException {
        int fromAccountId = scannerWrapper.getUserInput("Enter the from account id ", Integer::valueOf);
        int toAccountId = scannerWrapper.getUserInput("Enter the to account id ", Integer::valueOf);
        BigDecimal amount = scannerWrapper.getUserInput("Enter the amount ", BigDecimal::new);
        Currency currency = getCurrency();
        accountFacade.transfer(fromAccountId, toAccountId, new AmountDto(currency, amount));
    }

    private void withdraw() throws AccountNotFindException, ValidationException {
        int accountId = scannerWrapper.getUserInput("Enter the account id ", Integer::valueOf);
        BigDecimal amount = scannerWrapper.getUserInput("Enter the amount ", BigDecimal::new);
        Currency currency = getCurrency();
        accountFacade.withdraw(accountId, new AmountDto(currency, amount));

    }


    private void deposit() throws AccountNotFindException {
        int accountId = scannerWrapper.getUserInput("Enter the account id ", Integer::valueOf);
        BigDecimal amount = scannerWrapper.getUserInput("Enter the amount ", BigDecimal::new);
        Currency currency = getCurrency();
        accountFacade.deposit(accountId, new AmountDto(currency, amount));
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
        System.out.println("1.EUR");
        System.out.println("2.USD");
        System.out.println();
        int choice = scannerWrapper.getUserInput("Enter your choice: ", Integer::valueOf);
        try {
            Currency currency;
            if (choice == 1) {
                currency = Currency.getInstance("EUR");
            } else {
                currency = Currency.getInstance("USD");
            }
            int number = scannerWrapper.getUserInput("Enter Customer id : ", Integer::valueOf);
            AccountDto accountDto = new AccountDto(null, new AmountDto(currency, BigDecimal.ZERO), number);
            accountFacade.addAccounts(accountDto);

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

    public void saveOnExit() {
        accountFacade.saveOnExit();
    }

    public void initData() {
        accountFacade.initData();
    }


    private Currency getCurrency() {
        while (true) {
            System.out.println("Currency:");
            System.out.println("1. EUR");
            System.out.println("2. USD");
            System.out.println("3. GBP");
            System.out.println();
            int choice = scannerWrapper.getUserInput("Enter your choice: ",Integer::valueOf);

            if (choice == 1) {
                return Currency.getInstance("EUR");
            } else if (choice == 2) {
                return Currency.getInstance("USD");
            } else if (choice == 3) {
                return Currency.getInstance("GBP");
            }

            System.out.println("Invalid currency choice.");
        }
    }
}
