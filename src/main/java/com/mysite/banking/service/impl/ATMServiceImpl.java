package com.mysite.banking.service.impl;

import com.mysite.banking.dao.ATMStockDao;
import com.mysite.banking.dao.impl.ATMStockDaoImpl;
import com.mysite.banking.model.ATMStock;
import com.mysite.banking.model.Amount;
import com.mysite.banking.service.ATMService;
import com.mysite.banking.service.AccountService;
import com.mysite.banking.service.exception.AccountNotFindException;
import com.mysite.banking.service.exception.ValidationException;

import java.math.BigDecimal;

public class ATMServiceImpl implements ATMService {

    public static final int[] BILL_VALUE = new int[]{5, 10, 20, 50, 100, 200, 500};

    private  ATMStockDao atmStockDao;
    private final AccountService accountService;

    private static final ATMServiceImpl INSTANCE;

    public static ATMServiceImpl getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new ATMServiceImpl();
    }

    private ATMServiceImpl() {
        atmStockDao = ATMStockDaoImpl.getInstance();
        accountService = AccountServiceImpl.getInstance();
        try {
            initializeStock();
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private int[] withdrawSpecificAmount(BigDecimal amount)
            throws ValidationException {

        int[] billsToWithdraw = new int[BILL_VALUE.length];
        BigDecimal remainingAmount = amount;

        for (int i = BILL_VALUE.length - 1; i >= 0; i--) {

            int denomination = BILL_VALUE[i];

            ATMStock stock = atmStockDao.findByDenomination(denomination);

            int quantity = stock == null ? 0 : stock.getQuantity();
            BigDecimal denominationDecimal = BigDecimal.valueOf(denomination);

            int numBillsNeeded = remainingAmount
                    .divideToIntegralValue(denominationDecimal)
                    .intValue();

            if (numBillsNeeded > quantity) {
                numBillsNeeded = quantity;
            }

            billsToWithdraw[i] = numBillsNeeded;
            remainingAmount = remainingAmount.subtract(
                    denominationDecimal.multiply(BigDecimal.valueOf(numBillsNeeded)));
        }

        if (remainingAmount.compareTo(BigDecimal.ZERO) != 0) {
            throw new ValidationException("ATM cannot provide this amount!");
        }

        return billsToWithdraw;
    }

    private int calculateBalance() {

        int balance = 0;

        for (int denomination : BILL_VALUE) {

            ATMStock stock = atmStockDao.findByDenomination(denomination);

            if (stock != null) {
                balance += denomination * stock.getQuantity();
            }
        }

        return balance;
    }

    @Override
    public int[] withdraw(int accountId, Amount amount)
            throws AccountNotFindException, ValidationException {

        if (amount.getValue().compareTo(BigDecimal.valueOf(calculateBalance())) > 0) {
            throw new ValidationException("The amount is larger than ATM balance!");
        }

        int[] billsToWithdraw = withdrawSpecificAmount(amount.getValue());
        accountService.withdraw(accountId, amount);
        finalWithdrawSpecificAmount(billsToWithdraw);

        return billsToWithdraw;
    }

    private void finalWithdrawSpecificAmount(int[] billsToWithdraw) {

        for (int i = 0; i < BILL_VALUE.length; i++) {

            if (billsToWithdraw[i] > 0) {
                ATMStock stock = atmStockDao.findByDenomination(BILL_VALUE[i]);
                stock.setQuantity(stock.getQuantity() - billsToWithdraw[i]);
                atmStockDao.updateATM(stock);
            }
        }
    }


    @Override
    public void initializeStock() throws ValidationException {

        for (int denomination : BILL_VALUE) {

            if (atmStockDao.findByDenomination(denomination) == null) {

                ATMStock stock = new ATMStock();
                stock.setDenomination(denomination);
                stock.setQuantity(50);

                atmStockDao.saveATM(stock);
            }
        }
    }
}