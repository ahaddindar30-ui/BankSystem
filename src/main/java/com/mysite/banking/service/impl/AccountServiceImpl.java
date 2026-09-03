package com.mysite.banking.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.banking.dao.AccountDao;
import com.mysite.banking.dao.impl.AccountDaoImpl;
import com.mysite.banking.model.Account;
import com.mysite.banking.model.Amount;
import com.mysite.banking.model.Customer;
import com.mysite.banking.service.AccountService;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.util.AmountUtil;
import com.mysite.banking.util.MapperWrapper;

import java.io.*;

import java.util.List;


public class AccountServiceImpl implements AccountService {
    private final ObjectMapper objectMapper;

    private AmountUtil amountUtil;
    private AccountDao accountDao;


    private static final AccountServiceImpl INSTANCE;

    public static AccountServiceImpl getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new AccountServiceImpl();
    }

    private AccountServiceImpl() {
        this.objectMapper = MapperWrapper.getInstance();
        this.amountUtil = AmountUtil.getInstance();
        this.accountDao = AccountDaoImpl.getInstance();


    }


    @Override
    public void deleteAccountById(Integer id) throws AccountNotFindException {
        Account accountById = getAccountById(id);
        accountDao.deleteAccount(accountById);
    }

    @Override
    public void addAccounts(Account account) {
        accountDao.saveAccount(account);
    }


    @Override
    public List<Account> getActiveAccounts() throws EmptyAccountException {
        List<Account> accountList = accountDao.getAccountByStatus(false);
        if (accountList.isEmpty()) {
            throw new EmptyAccountException();
        } else {
            return accountList;
        }
    }

    @Override
    public List<Account> getDeletedAccounts() throws EmptyAccountException {
        List<Account> accountList = accountDao.getAccountByStatus(true);
        if (accountList.isEmpty()) {
            throw new EmptyAccountException();
        } else {
            return accountList;
        }
    }

    @Override
    public Account getAccountById(Integer id) throws AccountNotFindException {
        Account accountById = accountDao.findAccountById(id);
        if (accountById == null) {
            throw new AccountNotFindException();
        } else {
            return accountById;
        }

    }


    @Override
    public List<Account> getAccountByCustomerId(Integer id) {
        return accountDao.getByCustomerId(id);


    }

    public void deposit(int accountId, Amount amount) throws AccountNotFindException {
        Account accountById = getAccountById(accountId);
        accountById.setBalance(amountUtil.add(accountById.getBalance(), amount));
        accountDao.updateAccount(accountById);


    }

    public void withdraw(int accountId, Amount amount) throws AccountNotFindException, ValidationException {
        Account accountById = getAccountById(accountId);
        if (amountUtil.compareTo(amount, accountById.getBalance()) > 0) {
            throw new ValidationException("The amount is larger than balance!");
        }
        accountById.setBalance(amountUtil.subtract(accountById.getBalance(), amount));
        accountDao.updateAccount(accountById);


    }

    public void transfer(int fromAccountId, int toAccountId, Amount amount) throws AccountNotFindException, ValidationException {
        Account fromAccount = getAccountById(fromAccountId);
        Account toAccount = getAccountById(toAccountId);
        if (amountUtil.compareTo(amount, fromAccount.getBalance()) > 0) {
            throw new ValidationException("The amount is larger than from account balance!");
        }
        fromAccount.setBalance(amountUtil.subtract(fromAccount.getBalance(), amount));
        toAccount.setBalance(amountUtil.add(toAccount.getBalance(), amount));
        accountDao.updateAccount(fromAccount);
        accountDao.updateAccount(toAccount);


    }

    @Override
    public void exportFileJson(String fileName) throws FileException {
        File file = new File(fileName + ".json");
        try {
            List<Account> accountList = accountDao.getAllAccounts(null);
            objectMapper.writeValue(file, accountList);
        } catch (IOException e) {
            throw new FileException();
        }
    }


}

