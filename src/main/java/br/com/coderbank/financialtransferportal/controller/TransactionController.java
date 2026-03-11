package br.com.coderbank.financialtransferportal.controller;

import br.com.coderbank.financialtransferportal.dto.request.TransactionRequestDto;
import br.com.coderbank.financialtransferportal.dto.response.TransactionResponseDto;
import br.com.coderbank.financialtransferportal.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transactions")
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> executeTransaction(@RequestBody @Valid TransactionRequestDto transactionRequestDto){
        return ResponseEntity.ok(service.executeTransaction(transactionRequestDto));
    }
}
