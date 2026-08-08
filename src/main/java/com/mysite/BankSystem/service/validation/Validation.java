package com.mysite.BankSystem.service.validation;

import com.mysite.BankSystem.service.exception.ValidationException;

public interface Validation <T>{

    void validate(T t)throws ValidationException;


}
