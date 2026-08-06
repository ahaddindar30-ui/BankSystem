package com.mysite.BankSystem.service.exception;

public class EmptyCustomerException extends CustomerBaseException {
    public EmptyCustomerException() {
        super("there is not customer.");
    }
}
