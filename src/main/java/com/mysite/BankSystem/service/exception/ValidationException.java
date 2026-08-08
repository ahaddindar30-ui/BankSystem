package com.mysite.BankSystem.service.exception;

public class ValidationException extends CustomerBaseException{
    public ValidationException(String message) {
        super(message);

    }


    public ValidationException() {
        super();
    }
}
