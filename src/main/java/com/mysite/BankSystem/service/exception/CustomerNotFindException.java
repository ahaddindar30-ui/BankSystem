package com.mysite.BankSystem.service.exception;

public class CustomerNotFindException extends CustomerBaseException {
    public CustomerNotFindException() {
        super("Customer Not Found Exception.");
    }
}
