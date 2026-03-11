package br.com.coderbank.financialtransferportal.service;

import br.com.coderbank.financialtransferportal.dto.request.AccountRequestDto;
import br.com.coderbank.financialtransferportal.dto.response.AccountResponseDto;
import br.com.coderbank.financialtransferportal.entity.Account;
import br.com.coderbank.financialtransferportal.mapper.AccountMapper;
import br.com.coderbank.financialtransferportal.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;

    public AccountResponseDto create(AccountRequestDto accountRequestDto) {
        Account accountEntity = mapper.toEntity(accountRequestDto);

        repository.save(accountEntity);

        return mapper.toDto(accountEntity);
    }
}
