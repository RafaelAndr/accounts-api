package br.com.coderbank.financialtransferportal.dto.response;

import br.com.coderbank.financialtransferportal.enums.TransactionStatus;
import br.com.coderbank.financialtransferportal.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponseDto(
        UUID id,
        TransactionType type,
        BigDecimal amount,
        LocalDate dateTime,
        TransactionStatus status
) {
}
