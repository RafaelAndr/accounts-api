package br.com.coderbank.financialtransferportal.mapper;

import br.com.coderbank.financialtransferportal.dto.request.TransactionRequestDto;
import br.com.coderbank.financialtransferportal.dto.response.TransactionResponseDto;
import br.com.coderbank.financialtransferportal.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction toEntity(TransactionRequestDto transactionRequestDto);

    TransactionResponseDto toDto(Transaction transaction);
}
