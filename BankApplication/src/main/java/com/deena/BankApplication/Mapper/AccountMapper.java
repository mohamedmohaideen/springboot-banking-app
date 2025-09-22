package com.deena.BankApplication.Mapper;

import com.deena.BankApplication.BankRepository.TransactionRepository;
import com.deena.BankApplication.DTO.AccountDto;
import com.deena.BankApplication.DTO.TransactionDto;
import com.deena.BankApplication.Entity.Account;
import com.deena.BankApplication.Entity.Customer;
import com.deena.BankApplication.Entity.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class AccountMapper {

    public static AccountDto toDto(Account acc) {
        if (acc == null) return null;

        AccountDto dto = new AccountDto();
        dto.setId(acc.getId());
        dto.setHolderName(acc.getHolderName());
        dto.setBalance(acc.getBalance());
        dto.setCustomerId(acc.getCustomer() != null ? acc.getCustomer().getId() : null);

        if (acc.getTransactions() != null) {
            dto.setTransactions(
                    acc.getTransactions().stream()
                            .map(TransactionMapper::toDto)
                            .toList()
            );
        }
        return dto;
    }
    public static AccountDto toDto(Account acc, TransactionRepository txRepo) {
        AccountDto dto = new AccountDto();
        dto.setId(acc.getId());
        dto.setHolderName(acc.getHolderName());
        dto.setBalance(acc.getBalance());
        dto.setCustomerId(acc.getCustomer() != null ? acc.getCustomer().getId() : null);

        List<Transaction> transactions = txRepo.findByAccountIdOrderByTimestampDesc(acc.getId());
        dto.setTransactions(transactions.stream().map(TransactionMapper::toDto).toList());
        return dto;
    }

    public static Account toEntity(AccountDto dto) {
        if(dto == null) return null;
        Account account = new Account();
        account.setHolderName(dto.getHolderName());
        account.setBalance(dto.getBalance());
        // customer will be set in CustomerMapper
        return account;
    }


}
