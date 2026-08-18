package com.mysite.banking.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.banking.model.Account;
import com.mysite.banking.model.FileType;
import com.mysite.banking.service.AccountService;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.util.MapperWrapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {
    private ArrayList<Account> accounts;
    private final ObjectMapper objectMapper;


    private static final AccountServiceImpl INSTANCE;

    public static AccountServiceImpl getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new AccountServiceImpl();
    }

    private AccountServiceImpl() {
        this.accounts = new ArrayList<>();
        this.objectMapper = MapperWrapper.getInstance();


    }


    @Override
    public void deleteAccountById(Integer id) throws AccountNotFindException {
        getAccountById(id).setDeleted(true);
    }

    @Override
    public void addAccounts(Account account) {
        accounts.add(account);
    }


    @Override
    public List<Account> getActiveAccounts() throws EmptyAccountException {
        List<Account> collect = accounts.stream()
                .filter(account -> !account.isDeleted())
                .collect(Collectors.toList());
        if (collect.isEmpty()) {
            throw new EmptyAccountException();
        }
        return collect;
    }

    @Override
    public List<Account> getDeletedAccounts() throws EmptyAccountException {
        List<Account> collect = accounts.stream()
                .filter(Account::isDeleted)
                .collect(Collectors.toList());
        if (collect.isEmpty()) {
            throw new EmptyAccountException();
        }
        return collect;
    }

    @Override
    public Account getAccountById(Integer id) throws AccountNotFindException {
        return accounts.stream()
                .filter(account -> !account.isDeleted())
                .filter(account -> account.getId().equals(id))
                .findFirst().orElseThrow(AccountNotFindException::new);
    }

    @Override
    public void saveData(String name, FileType fileType) throws FileException {
        switch (fileType) {
            case FileType.JSON -> saveJson(name);
            case FileType.SERIALIZE -> saveSerialize(name);
        }


    }

    private void saveJson(String name) throws FileException {
        try {
            File file = new File(name + ".json");
            if (!file.exists()) {
                file.createNewFile();
            }
            objectMapper.writeValue(file, accounts);
        } catch (IOException e) {
            throw new FileException();
        }

    }

    private void saveSerialize(String name) throws FileException {
        try {
            File file = new File(name + ".crm");
            if (!file.exists()) {
                file.createNewFile();
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                objectOutputStream.writeObject(accounts);
            }
        } catch (IOException e) {
            throw new FileException();
        }
    }

    @Override
    public void loadData(String name, FileType fileType) throws FileException {
        switch (fileType) {
            case FileType.JSON -> loadJson(name);
            case FileType.SERIALIZE -> loadSerialize(name);
        }

    }

    @Override
    public void initData() {
        try {
            loadJson("init Account Data");
        } catch (FileException ignored) {

        }
    }

    @Override
    public void saveOnExit() {
        try {
            saveJson("init Account Data");
        } catch (FileException ignored) {

        }
    }

    @Override
    public void addData(String name) throws FileException {
        try {
            ArrayList<Account> newAccounts = objectMapper.readValue(new File(name + ".json"),
                    new TypeReference<ArrayList<Account>>() {
                    });
            accounts.addAll(newAccounts);
        } catch (IOException e) {
            throw new FileException();
        }
    }

    @Override
    public List<Account> getAccountByCustomerId(Integer id) {
        return accounts.stream()
                .filter(account -> !account.isDeleted())
                .filter(account -> account.getCustomerId().equals(id))
                .collect(Collectors.toList());

    }

    @Override
    public void deposit(int accountId, Double amount) throws AccountNotFindException {
        Account accountById = getAccountById(accountId);
        accountById.setBalance(accountById.getBalance() + amount);
    }

    @Override
    public void withdraw(int accountId, Double amount) throws AccountNotFindException, ValidationException {
        Account accountById = getAccountById(accountId);
        if (amount > accountById.getBalance()) {
            throw new ValidationException("The amount is larger than balance!");
        }
        accountById.setBalance(accountById.getBalance() - amount);
    }

    private void loadSerialize(String name) throws FileException {
        try (FileInputStream fileInputStream = new FileInputStream(name + ".crm");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            accounts = (ArrayList<Account>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new FileException();
        }
    }


    private void loadJson(String name) throws FileException {
        try {
            accounts = objectMapper.readValue(new File(name + ".json"),
                    new TypeReference<ArrayList<Account>>() {
                    });
        } catch (IOException e) {
            throw new FileException();
        }
    }

}

