package com.mySite.banking.facade;


import com.mysite.banking.dto.AccountDto;
import com.mysite.banking.dto.AmountDto;
import com.mysite.banking.facade.AccountFacade;
import com.mysite.banking.facade.impl.AccountFacadeImpl;
import com.mysite.banking.facade.impl.CustomerFacadeImpl;
import com.mysite.banking.model.Account;
import com.mysite.banking.model.RealCustomer;
import com.mysite.banking.service.AccountService;
import com.mysite.banking.service.CustomerService;
import com.mysite.banking.service.exception.CustomerNotFindException;
import com.mysite.banking.service.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountFacadeTest {
    @Mock
    private CustomerService customerService;
    @Mock
    private AccountService accountService;


    private AccountFacade accountFacade;


    @BeforeEach
    public void setup() {
        accountFacade = AccountFacadeImpl.getInstance();
        CustomerFacadeImpl.getInstance(customerService);
        AccountFacadeImpl.getInstance(accountService);
    }

    @Test
    public void addAccountTest() throws CustomerNotFindException {
        AccountDto accountDto = new AccountDto();
        accountDto.setCustomerId(10);
        accountDto.setBalance(new AmountDto(Currency.getInstance("USD"), BigDecimal.ZERO));
        when(customerService.getCustomerById(10))
                .thenThrow(new CustomerNotFindException());
        Exception exception = assertThrows(ValidationException.class, () -> {
            accountFacade.addAccounts(accountDto);
        });
        assertEquals("Customer id is not valid.", exception.getMessage());


    }

    @Test
    public void addAccount() throws CustomerNotFindException, ValidationException {
        AccountDto accountDto = new AccountDto();
        accountDto.setCustomerId(10);
        accountDto.setBalance(new AmountDto(Currency.getInstance("USD"), BigDecimal.ZERO));
        when(customerService.getCustomerById(10))
                .thenReturn(new RealCustomer());


        accountFacade.addAccounts(accountDto);


        verify(accountService)
                .addAccounts(any(Account.class));


    }
}
