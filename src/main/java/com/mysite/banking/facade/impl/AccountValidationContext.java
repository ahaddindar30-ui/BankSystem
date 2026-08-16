package com.mysite.banking.facade.impl;

import com.mysite.banking.dto.AccountDto;

import com.mysite.banking.service.exception.ValidationException;
import com.mysite.banking.service.validation.ValidationContext;


public class AccountValidationContext extends ValidationContext<AccountDto> {

    public AccountValidationContext() {
        addValidation(accountDto -> {
            Double balance = accountDto.getBalance();
            if (balance < 0) {
                throw new ValidationException("Balance can not by lass than zero.");
            }
        });

    }


}
