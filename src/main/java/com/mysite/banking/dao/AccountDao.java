package com.mysite.banking.dao;

import com.mysite.banking.model.Account;
import com.mysite.banking.model.Customer;

import java.util.List;

public interface AccountDao {

    Integer saveAccount(Account account);
    void updateAccount(Account account);
    void deleteAccount(Account account);
    Account findAccountById(Integer id);
    List<Account> getAccountByStatus(Boolean deleted);
    List<Account> getAllAccounts(Boolean deleted);
    List<Account> getByCustomerId(Integer customerId);


}
