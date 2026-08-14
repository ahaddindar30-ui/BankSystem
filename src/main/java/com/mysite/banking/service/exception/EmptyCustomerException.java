package com.mysite.banking.service.exception;

public class EmptyCustomerException extends CustomerBaseException {
    public EmptyCustomerException() {
        super("there is not customer.");
    }
}
