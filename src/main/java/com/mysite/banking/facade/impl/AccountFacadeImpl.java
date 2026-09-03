package com.mysite.banking.facade.impl;

import com.mysite.banking.dto.AccountDto;
import com.mysite.banking.dto.AmountDto;
import com.mysite.banking.facade.AccountFacade;
import com.mysite.banking.mapper.AccountMapStruct;
import com.mysite.banking.model.Account;
import com.mysite.banking.model.Customer;
import com.mysite.banking.service.AccountService;
import com.mysite.banking.service.CustomerService;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.service.impl.AccountServiceImpl;
import com.mysite.banking.service.impl.CustomerServiceImpl;
import com.mysite.banking.service.validation.ValidationContext;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

public class AccountFacadeImpl implements AccountFacade {


    private ValidationContext<AccountDto> validationContext;

    private AccountService accountService;
    private final CustomerService customerService;
    private final AccountMapStruct accountMapStruct;


    private static final AccountFacadeImpl INSTANCE;

    public static AccountFacadeImpl getInstance() {
        return INSTANCE;
    }
    public static AccountFacadeImpl getInstance(AccountService accountService) {
        INSTANCE.accountService = accountService;
        return INSTANCE;
    }

    static {
        INSTANCE = new AccountFacadeImpl();
    }

    private AccountFacadeImpl() {
        this.accountMapStruct = Mappers.getMapper(AccountMapStruct.class);
        this.accountService = AccountServiceImpl.getInstance();
        this.customerService = CustomerServiceImpl.getInstance();
        this.validationContext = new AccountValidationContext();
    }


    @Override
    public void deleteAccountById(Integer id) throws AccountNotFindException {
        accountService.deleteAccountById(id);
    }


    @Override
    public void addAccounts(AccountDto accountDto) throws ValidationException {
        validationContext.validate(accountDto);
        accountService.addAccounts(accountMapStruct.mapToAccount(accountDto));

    }

    @Override
    public void updateAccount(AccountDto accountDto) throws ValidationException, AccountNotFindException {
        validationContext.validate(accountDto);
        Account account = accountService.getAccountById(accountDto.getId());
        accountMapStruct.mapToAccount(accountDto, account);

    }

    @Override
    public List<AccountDto> getActiveAccounts() throws EmptyAccountException {
        return accountMapStruct.mapAccountDtoList(
                accountService.getActiveAccounts());
    }

    @Override
    public List<AccountDto> getDeletedAccounts() throws EmptyAccountException {
        return accountMapStruct.mapAccountDtoList(
                accountService.getDeletedAccounts());
    }

    @Override
    public AccountDto getAccountById(Integer id) throws AccountNotFindException {
        return accountMapStruct.mapToAccountDto(accountService.getAccountById(id));
    }


    @Override
    public List<AccountDto> printAccountByCustomerName(String name) {
        List<Customer> customers = customerService.printCustomersByName(name);
        List<Account> accountList = new ArrayList<>();
        for (Customer customer : customers) {
            accountList.addAll(accountService.getAccountByCustomerId(customer.getId()));
        }
        return accountMapStruct.mapAccountDtoList(accountList);
    }

    @Override
    public void deposit(int accountId, AmountDto amount) throws AccountNotFindException {
        accountService.deposit(accountId, accountMapStruct.mapToAmount(amount));
    }

    @Override
    public void withdraw(int accountId, AmountDto amount) throws AccountNotFindException, ValidationException {
        accountService.withdraw(accountId, accountMapStruct.mapToAmount(amount));
    }

    @Override
    public void transfer(int fromAccountId, int toAccountId, AmountDto amountDto) throws AccountNotFindException, ValidationException {
        accountService.transfer(fromAccountId, toAccountId, accountMapStruct.mapToAmount(amountDto));
    }

    @Override
    public void exportFileJson(String fileName) throws FileException {
        accountService.exportFileJson(fileName);
    }
}
