package com.mysite.banking.facade;

import com.mysite.banking.dto.AmountDto;
import com.mysite.banking.dto.WithdrawDto;
import com.mysite.banking.service.exception.AccountNotFindException;
import com.mysite.banking.service.exception.ValidationException;


public interface ATMFacade {

    WithdrawDto withdraw(int accountId, AmountDto amount)throws AccountNotFindException, ValidationException;}
