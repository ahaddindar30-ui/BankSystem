package com.mysite.banking.mapper;

import com.mysite.banking.dto.AccountDto;
import com.mysite.banking.dto.AmountDto;
import com.mysite.banking.model.Account;
import com.mysite.banking.model.Amount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface AccountMapStruct {

    List<AccountDto> mapAccountDtoList(List<Account> accountList);

    AccountDto mapToAccountDto(Account account);

    @Mapping(target = "id", ignore = true)
    Account mapToAccount(AccountDto accountDto,
                         @MappingTarget Account account);

    Amount mapToAmount(AmountDto amountDto);

    @Mapping(target = "id", ignore = true)
    Account mapToAccount(AccountDto accountDto);
}
