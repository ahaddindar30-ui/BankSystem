package com.mysite.banking.facade.impl;

import com.mysite.banking.dto.AccountDto;
import com.mysite.banking.facade.AccountFacade;
import com.mysite.banking.mapper.AccountMapStruct;
import com.mysite.banking.model.Account;
import com.mysite.banking.model.FileType;
import com.mysite.banking.service.AccountService;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.service.impl.AccountServiceImpl;
import com.mysite.banking.service.validation.ValidationContext;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class AccountFacadeImpl implements AccountFacade {


    private ValidationContext<AccountDto> validationContext;

    private final AccountService accountService;
    private final AccountMapStruct accountMapStruct;


    private static final AccountFacadeImpl INSTANCE;

    public static AccountFacadeImpl getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new AccountFacadeImpl();
    }

    private AccountFacadeImpl() {
        this.accountMapStruct = Mappers.getMapper(AccountMapStruct.class);
        this.accountService = AccountServiceImpl.getInstance();
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

    public void saveData(String name, FileType fileType) throws FileException {
        accountService.saveData(name ,fileType);
    }

    @Override
    public void loadData(String name, FileType fileType) throws FileException {
        accountService.loadData(name ,fileType);
    }

    @Override
    public void initData() {
        accountService.initData();
    }

    @Override
    public void saveOnExit() {
        accountService.saveOnExit();
    }

    @Override
    public void addData(String name) throws FileException {
        accountService.addData(name);
    }
}
