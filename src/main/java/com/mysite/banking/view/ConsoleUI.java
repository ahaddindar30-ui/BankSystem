package com.mysite.banking.view;


import com.mysite.banking.service.exception.*;

import com.mysite.banking.view.component.AccountConsole;
import com.mysite.banking.view.component.BaseConsole;
import com.mysite.banking.view.component.CustomerConsole;



public class ConsoleUI extends BaseConsole implements AutoCloseable {
  private final CustomerConsole customerConsole;
  private final AccountConsole accountConsole;

    public ConsoleUI() {
        super();
        this.customerConsole = new CustomerConsole();
        this.accountConsole = new AccountConsole();
    }

    private void saveOnExit() {
        customerConsole.saveOnExit();
        accountConsole.saveOnExit();
    }
    public void printMainMenu() {
        System.out.println("Menu:");
        System.out.println("0.Exit");
        System.out.println("1.Customer Management");
        System.out.println("2.Account Management");
        System.out.println();
    }
    public void startMenu() {
        customerConsole.initData();
        accountConsole.initData();
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
                    customerConsole.menu();
                    break;
                case 2:
                    accountConsole.menu();
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        } while (choice != 0);
        scannerWrapper.close();


    }







    @Override
    public void close() {
        scannerWrapper.close();
    }
}
