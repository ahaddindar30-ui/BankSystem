package com.mysite.banking.service.validation;

import com.mysite.banking.service.exception.ValidationException;

public interface Validation<T> {

    void validate(T t) throws ValidationException;


}
