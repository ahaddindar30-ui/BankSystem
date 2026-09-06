package com.mysite.banking.view;


public class ConsoleUI extends BaseConsole implements AutoCloseable {
    private final CustomerConsole customerConsole;
    private final AccountConsole accountConsole;
    private final AtmConsole atmConsole;

    public ConsoleUI() {
        super();
        this.customerConsole = new CustomerConsole();
        this.accountConsole = new AccountConsole();
        atmConsole = new AtmConsole();
    }


    public void printMainMenu() {
        System.out.println();
        System.out.println("===== MAIN MENU =====");
        System.out.println("0.Exit");
        System.out.println("1.Customer Management");
        System.out.println("2.Account Management");
        System.out.println("3.ATM");
        System.out.println();
    }

    public void startMenu() {

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
                case 3:
                    atmConsole.menu();
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
