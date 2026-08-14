package com.mysite.banking.service.exception;

public class CustomerBaseException extends Exception {
    public CustomerBaseException() {
    }

    public CustomerBaseException(String message) {
        super(message);
    }
}
