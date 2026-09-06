package com.mysite.banking.facade.impl;

import com.mysite.banking.dto.AmountDto;
import com.mysite.banking.dto.WithdrawDto;
import com.mysite.banking.facade.ATMFacade;
import com.mysite.banking.mapper.AccountMapStruct;
import com.mysite.banking.service.ATMService;

import com.mysite.banking.service.exception.AccountNotFindException;
import com.mysite.banking.service.exception.ValidationException;
import com.mysite.banking.service.impl.ATMServiceImpl;

import org.mapstruct.factory.Mappers;


public class ATMFacadeImpl implements ATMFacade {
    private final ATMService atmService;
    private final AccountMapStruct accountMapStruct;


    private static final ATMFacadeImpl INSTANCE;

    public static ATMFacadeImpl getInstance() {
        return INSTANCE;
    }


    static {
        INSTANCE = new ATMFacadeImpl();
    }

    private ATMFacadeImpl() {
        this.accountMapStruct = Mappers.getMapper(AccountMapStruct.class);
        this.atmService = ATMServiceImpl.getInstance();

    }

    @Override
    public WithdrawDto withdraw(int accountId, AmountDto amount)
            throws AccountNotFindException, ValidationException {
        int[] quantities = atmService.withdraw(accountId, accountMapStruct.mapToAmount(amount));

        return new WithdrawDto(ATMServiceImpl.BILL_VALUE, quantities);
    }
}
