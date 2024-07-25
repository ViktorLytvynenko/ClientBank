package org.example.clientbank.account.api.dto;

import org.example.clientbank.account.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(source = "number", target = "number")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "balance", target = "balance")
    Account accountDtoToAccount(RequestAccountDto requestAccountDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "balance", target = "balance")
    ResponseAccountDto accountToAccountDto(Account account);
}
