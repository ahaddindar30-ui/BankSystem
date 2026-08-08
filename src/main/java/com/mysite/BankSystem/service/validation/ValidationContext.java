package com.mysite.BankSystem.service.validation;

import com.mysite.BankSystem.model.Customer;
import com.mysite.BankSystem.service.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class ValidationContext <T>{
    private List<Validation<T>>validations;


   public  ValidationContext() {
       this.validations = new ArrayList<>();
   }

   public void addValidation(Validation<T> validation){
       validations.add(validation);
   }

    public void validate (T obgect)throws ValidationException {
        for (Validation<T> validation : validations) {
            validation.validate(obgect);
        }

    }


}
