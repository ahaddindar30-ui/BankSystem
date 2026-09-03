package com.mySite.banking.service;

import com.mysite.banking.model.Account;
import com.mysite.banking.model.Amount;
import com.mysite.banking.service.AccountService;
import com.mysite.banking.service.exception.AccountNotFindException;
import com.mysite.banking.service.exception.UpdateException;
import com.mysite.banking.service.exception.ValidationException;
import com.mysite.banking.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;


public class AccountServiceTest {

    private AccountService accountService;

    static {
        System.setProperty("DB_MEM", "true");
    }

    @BeforeEach
    public void setup() {
        accountService = AccountServiceImpl.getInstance();
    }


    @Test
    public void depositCaseA() throws AccountNotFindException {
        Account account = new Account();
        account.setBalance(new Amount(Currency.getInstance("USD"), BigDecimal.ZERO));
        accountService.addAccounts(account);
        Integer id = account.getId();
        accountService.deposit(id, new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(10.0)));
        Account accountById = accountService.getAccountById(id);

        assertEquals(BigDecimal.valueOf(10.0).setScale(2), accountById.getBalance().getValue());


    }

    @Test
    public void depositCaseB() throws AccountNotFindException {
        Account account = new Account();
        account.setBalance(new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(20.0)));
        accountService.addAccounts(account);
        Integer id = account.getId();
        accountService.deposit(id, new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(10.0)));
        Account accountById = accountService.getAccountById(id);

        assertEquals(BigDecimal.valueOf(30.0).setScale(2), accountById.getBalance().getValue());


    }


    @Test
    public void depositCaseC() throws AccountNotFindException {
        Account account = new Account();
        account.setBalance(new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(20.2)));
        accountService.addAccounts(account);
        Integer id = account.getId();
        accountService.deposit(id, new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(10.1)));
        Account accountById = accountService.getAccountById(id);

        assertEquals(BigDecimal.valueOf(30.3).setScale(2), accountById.getBalance().getValue());


    }

    @Test
    public void depositCaseD() throws AccountNotFindException {
        Account account = new Account();
        account.setBalance(new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(50.1)));
        accountService.addAccounts(account);
        Integer id = account.getId();
        accountService.deposit(id, new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(30.2)));
        Account accountById = accountService.getAccountById(id);

        assertEquals(BigDecimal.valueOf(80.3).setScale(2), accountById.getBalance().getValue());


    }

    @Test
    public void depositCaseE() throws AccountNotFindException {
        Account account = new Account();
        account.setBalance(new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(2.1)));
        accountService.addAccounts(account);
        Integer id = account.getId();
        accountService.deposit(id, new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(3.2)));
        Account accountById = accountService.getAccountById(id);

        assertEquals(BigDecimal.valueOf(5.3).setScale(2), accountById.getBalance().getValue());


    }

    @Test
    public void depositCaseF() throws AccountNotFindException {
        Account account = new Account();
        account.setBalance(new Amount(Currency.getInstance("USD"), BigDecimal.ZERO));

        accountService.addAccounts(account);
        Integer id = account.getId();
        for (int i = 0; i < 10; i++) {
            accountService.deposit(id, new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(10.0)));
        }
        Account accountById = accountService.getAccountById(id);

        assertEquals(BigDecimal.valueOf(100.0).setScale(2), accountById.getBalance().getValue());


    }


    @Test
    public void depositCaseG() throws AccountNotFindException {
        Account account = new Account();
        account.setBalance(new Amount(Currency.getInstance("USD"), BigDecimal.ZERO));
        accountService.addAccounts(account);
        Integer id = account.getId();
        Runnable runnable = () -> {
            for (int i = 0; i < 10; i++) {
                try {
                    accountService.deposit(id, new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(100.0)));
                } catch (AccountNotFindException ignored) {

                }
            }

        };

        runnable.run();


        Account accountById = accountService.getAccountById(id);

        assertEquals(BigDecimal.valueOf(1000.0).setScale(2), accountById.getBalance().getValue());


    }

    @Test
    public void depositCaseH() throws AccountNotFindException, InterruptedException {
        Account account = new Account();
        account.setBalance(new Amount(Currency.getInstance("USD"), BigDecimal.ZERO));

        accountService.addAccounts(account);
        Integer id = account.getId();
        Runnable depositTask = () -> {
            for (int i = 0; i < 100; i++) {
                try {
                    accountService.deposit(id, new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(10.0)));
                } catch (AccountNotFindException ignored) {

                }
            }

        };

        Thread[] threads = new Thread[1];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(depositTask);
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();

        }


        Account accountById = accountService.getAccountById(id);

        assertEquals(BigDecimal.valueOf(1000.0).setScale(2), accountById.getBalance().getValue());


    }


    @Test
    public void depositCaseI() throws AccountNotFindException, InterruptedException {
        Account account = new Account();
        account.setBalance(new Amount(Currency.getInstance("USD"), BigDecimal.ZERO));
        accountService.addAccounts(account);
        Integer id = account.getId();
        Runnable depositTask = () -> {
            for (int i = 0; i < 100; i++) {
                try {
                    accountService.deposit(id, new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(10.0)));
                } catch (UpdateException e) {
                    i = i - 1;
                } catch (AccountNotFindException ignored) {

                }
            }

        };

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(depositTask);
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();

        }


        Account accountById = accountService.getAccountById(id);

        assertEquals(BigDecimal.valueOf(10000.0).setScale(2), accountById.getBalance().getValue());


    }

    @Test
    public void withdrawCaseA() throws AccountNotFindException, ValidationException {
        Account account = new Account();
        account.setBalance(new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(20.3)));

        accountService.addAccounts(account);
        Integer id = account.getId();
        accountService.withdraw(id, new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(10.2)));
        Account accountById = accountService.getAccountById(id);

        assertEquals(BigDecimal.valueOf(10.1).setScale(2), accountById.getBalance().getValue());


    }

    @Test
    public void withdrawCaseB() throws AccountNotFindException, InterruptedException {
        Account account = new Account();
        account.setBalance(new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(30000)));

        accountService.addAccounts(account);
        Integer id = account.getId();
        Runnable withdrawTask = () -> {
            for (int i = 0; i < 100; i++) {
                try {
                    accountService.withdraw(id, new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(10.0)));
                } catch (UpdateException e) {
                    i = i - 1;
                } catch (AccountNotFindException | ValidationException ignored) {

                }
            }

        };

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(withdrawTask);
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();

        }


        Account accountById = accountService.getAccountById(id);

        assertEquals(BigDecimal.valueOf(20000.0).setScale(2), accountById.getBalance().getValue());


    }

    @Test
    public void transferCaseA() throws AccountNotFindException, ValidationException {
        Account accountA = new Account();
        accountA.setBalance(new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(10.1)));
        accountService.addAccounts(accountA);
        Integer idA = accountA.getId();

        Account accountB = new Account();
        accountB.setBalance(new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(20.2)));

        accountService.addAccounts(accountB);
        Integer idB = accountB.getId();


        accountService.transfer(idA, idB, new Amount(Currency.getInstance("USD"), BigDecimal.valueOf(10.1)));


        Account accountById = accountService.getAccountById(idB);

        assertEquals(BigDecimal.valueOf(30.3).setScale(2), accountById.getBalance().getValue());


    }


}
