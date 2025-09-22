package com.deena.BankApplication.Controller;

import com.deena.BankApplication.BankRepository.TransactionRepository;
import com.deena.BankApplication.BankService.TransactionService;
import com.deena.BankApplication.DTO.TransactionDto;
import com.deena.BankApplication.Entity.Transaction;
import com.deena.BankApplication.Mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/account/{id}")
    public ResponseEntity<Page<TransactionDto>> getTransactionsByAccount(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Page<TransactionDto> transactions = transactionService.getTransactionsByAccount(id, page, size, sortBy, sortDir);
        return ResponseEntity.ok(transactions);
    }
}


