package com.mySite.banking.service;

import com.mysite.banking.dao.ATMStockDao;
import com.mysite.banking.dao.impl.ATMStockDaoImpl;
import com.mysite.banking.model.ATMStock;
import com.mysite.banking.model.Account;
import com.mysite.banking.model.Amount;
import com.mysite.banking.service.ATMService;
import com.mysite.banking.service.AccountService;
import com.mysite.banking.service.exception.AccountNotFindException;
import com.mysite.banking.service.exception.ValidationException;
import com.mysite.banking.service.impl.ATMServiceImpl;
import com.mysite.banking.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class ATMServiceTest {

    private ATMService atmService;
    private  ATMStockDao atmStockDao;
    private  AccountService accountService;
    static {
        System.setProperty("DB_MEM", "true");
    }
    @BeforeEach
    public void setup() {
        accountService= AccountServiceImpl.getInstance();
        atmService = ATMServiceImpl.getInstance();
        atmStockDao = ATMStockDaoImpl.getInstance();
        for (int denomination : ATMServiceImpl.BILL_VALUE) {

            ATMStock stock = atmStockDao.findByDenomination(denomination);

            if (stock != null) {
                stock.setQuantity(50);
                atmStockDao.updateATM(stock);
            }
        }
    }

    @Test
    void initializeStock_shouldCreateAllDenominationsWith50Bills()
            throws ValidationException {

        atmService.initializeStock();

        for (int denomination : ATMServiceImpl.BILL_VALUE) {

            ATMStock stock =
                    atmStockDao.findByDenomination(denomination);

            assertNotNull(stock);
            assertEquals(50, stock.getQuantity());
            assertEquals(denomination, stock.getDenomination());
        }
    }

    @Test
    void withdraw_shouldDecreaseAtmStock()
            throws AccountNotFindException, ValidationException {

        Account account = new Account();
        account.setBalance(new Amount(
                Currency.getInstance("EUR"),
                BigDecimal.valueOf(100)
        ));

        accountService.addAccounts(account);
        Integer accountId = account.getId();

        ATMStock stockBefore =
                atmStockDao.findByDenomination(50);

        int quantityBefore = stockBefore.getQuantity();

        atmService.withdraw(
                accountId,
                new Amount(
                        Currency.getInstance("EUR"),
                        BigDecimal.valueOf(50)
                )
        );

        ATMStock stockAfter =
                atmStockDao.findByDenomination(50);

        assertEquals(quantityBefore - 1, stockAfter.getQuantity());
    }
    @Test
    void withdraw_shouldThrowExceptionWhenAmountIsTooLarge()
            throws AccountNotFindException, ValidationException {

        atmService.initializeStock();

        assertThrows(
                ValidationException.class,
                () -> atmService.withdraw(
                        1,
                        new com.mysite.banking.model.Amount(
                                Currency.getInstance("EUR"),
                                BigDecimal.valueOf(1000000)
                        )
                )
        );
    }
}