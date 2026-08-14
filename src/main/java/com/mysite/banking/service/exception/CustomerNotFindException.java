package com.mysite.banking.service.exception;

public class CustomerNotFindException extends CustomerBaseException {
    public CustomerNotFindException() {
        super("Customer Not Found Exception.");
    }
}
