package com.mysite.BankSystem;

import com.mysite.BankSystem.view.ConsoleUI;

public class ApplicationRunner {


    public static void main(String[] args) {
        try (ConsoleUI consoleUI = new ConsoleUI()) {
            consoleUI.startMenu();

        }



    }

}