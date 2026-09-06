package com.mysite.banking.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysite.banking.dto.AccountDto;
import com.mysite.banking.dto.AmountDto;
import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.dto.WithdrawDto;
import com.mysite.banking.facade.ATMFacade;
import com.mysite.banking.facade.AccountFacade;
import com.mysite.banking.facade.CustomerFacade;
import com.mysite.banking.facade.impl.ATMFacadeImpl;
import com.mysite.banking.facade.impl.AccountFacadeImpl;
import com.mysite.banking.facade.impl.CustomerFacadeImpl;
import com.mysite.banking.service.exception.AccountNotFindException;
import com.mysite.banking.service.exception.ValidationException;


import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class AtmConsole extends BaseConsole {

    private final CustomerFacade customerFacade;
    private final AccountFacade accountFacade;
    private final ATMFacade atmFacade;

    private CustomerDto currentCustomer;

    public AtmConsole() {
        this.accountFacade = AccountFacadeImpl.getInstance();
        this.customerFacade = CustomerFacadeImpl.getInstance();
        this.atmFacade = ATMFacadeImpl.getInstance();
    }

    private void printMenu() {

        System.out.println();
        System.out.println("===== ATM MENU =====");
        System.out.println("1. Login");
        System.out.println("0. Back");
        System.out.println();
    }

    public void menu() {

        int choice;

        do {
            printMenu();

            choice = scannerWrapper.getUserInput("Enter Choice: ", Integer::valueOf);

            try {
                switch (choice) {

                    case 1:
                        login();
                        if (currentCustomer != null) {
                            loggedInMenu();
                        }
                        break;
                    case 0:
                        break;

                    default:
                        System.out.println("Invalid Choice");
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        } while (choice != 0);
    }


    private void login() {
        String userName = scannerWrapper.getUserInput("Enter your email: ", Function.identity());

        String password = scannerWrapper.getUserInput("Enter your password: ", Function.identity());

        currentCustomer = customerFacade.login(userName, password);

        if (currentCustomer != null) {
            System.out.println();
            System.out.println("Welcome to ATM system " + currentCustomer.getName());
        } else {
            System.out.println("Username or password is wrong!");
        }
    }


    private void printAtmMenu() {
        System.out.println();
        System.out.println("===== ATM =====");
        System.out.println("0. Back");
        System.out.println("1. Check Balance");
        System.out.println("2. Withdraw");
        System.out.println();
    }

    private void loggedInMenu() {

        int choice;

        do {
            printAtmMenu();
            choice = scannerWrapper.getUserInput("Enter Choice: ", Integer::valueOf);
            try {
                switch (choice) {

                    case 1:
                        checkBalance();
                        break;
                    case 2:
                        withdraw();
                        break;
                    case 0:
                        break;

                    default:
                        System.out.println("Invalid Choice");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }


        } while (choice != 0);


    }

    private void withdraw() throws AccountNotFindException, ValidationException {
        int accountId = scannerWrapper.getUserInput("Enter the account id ", Integer::valueOf);
        BigDecimal amount = scannerWrapper.getUserInput("Enter the amount ", BigDecimal::new);
        Currency currency = getCurrency();
        WithdrawDto withdrawDto = atmFacade.withdraw(accountId, new AmountDto(currency, amount));
        IntStream.range(0, withdrawDto.getQuantities().length)
                .filter(i -> withdrawDto.getQuantities()[i] > 0)
                .forEach(i -> System.out.println(withdrawDto.getQuantities()[i] + ", €: " + withdrawDto.getBillValues()[i]));

    }

    private Currency getCurrency() {
        while (true) {
            System.out.println("Currency:");
            System.out.println("1. EUR");
            System.out.println();
            int choice = scannerWrapper.getUserInput("Enter your choice: ", Integer::valueOf);
            if (choice == 1) {
                return Currency.getInstance("EUR");
            }
            System.out.println("Invalid currency choice.");
        }
    }

    private void checkBalance() {
        List<AccountDto> allAccount = accountFacade.printAccountById(currentCustomer.getId());
        System.out.println("All Accounts: ");
        for (AccountDto account : allAccount) {
            try {
                System.out.println(objectMapper.writeValueAsString(account));
            } catch (JsonProcessingException e) {
                System.out.println("Error on print account id " + account.getId());

            }
        }
    }


}