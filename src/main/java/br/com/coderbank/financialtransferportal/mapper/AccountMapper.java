package br.com.coderbank.financialtransferportal.mapper;

import br.com.coderbank.financialtransferportal.dto.request.AccountRequestDto;
import br.com.coderbank.financialtransferportal.dto.response.AccountResponseDto;
import br.com.coderbank.financialtransferportal.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toEntity(AccountRequestDto accountRequestDto);

    AccountResponseDto toDto(Account account);
}
