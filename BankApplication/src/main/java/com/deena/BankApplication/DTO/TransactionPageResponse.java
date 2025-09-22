package com.deena.BankApplication.DTO;

import com.deena.BankApplication.Entity.Transaction;

import java.util.List;

public record TransactionPageResponse(
        List<TransactionDto> transactions,
        Integer pageNumber,
        Integer pageSize,
        Long totalElements,
        Integer totalPages,
        boolean isLast
) {
}
