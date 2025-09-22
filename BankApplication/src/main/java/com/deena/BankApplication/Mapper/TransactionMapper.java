package com.deena.BankApplication.Mapper;

import com.deena.BankApplication.DTO.TransactionDto;
import com.deena.BankApplication.Entity.Account;
import com.deena.BankApplication.Entity.Transaction;

public class TransactionMapper {

    public static TransactionDto toDto(Transaction tx) {
        if (tx == null) return null;

        TransactionDto dto = new TransactionDto();
        dto.setId(tx.getId());
        dto.setAmount(tx.getAmount());
        dto.setType(tx.getType());
        dto.setTimestamp(tx.getTimestamp());

        // ✅ Handle orphan transactions gracefully
        if (tx.getAccount() != null) {
            dto.setAccountId(tx.getAccount().getId());
        } else {
            dto.setAccountId(null);
        }

        return dto;
    }

    public static Transaction toEntity(TransactionDto dto, Account account) {
        if (dto == null) return null;

        Transaction transaction = new Transaction();
        transaction.setId(dto.getId());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setTimestamp(dto.getTimestamp());

        // ✅ Only set account if provided
        transaction.setAccount(account);

        return transaction;
    }
}
