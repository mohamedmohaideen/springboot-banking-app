package com.deena.BankApplication.BankService;

import com.deena.BankApplication.DTO.AccountDto;
import com.deena.BankApplication.DTO.TransactionRequest;
import com.deena.BankApplication.Exception.AccountNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AccountService {

    ResponseEntity<List<AccountDto>> getAllAccounts();

    Page<AccountDto> getAllAccounts(Pageable pageable);

    ResponseEntity<Page<AccountDto>> getAccountsPaginated(int page, int size, String sortBy, String sortDir);

    ResponseEntity<AccountDto> createAccount(AccountDto dto);

    ResponseEntity<AccountDto> getById(Long id) throws AccountNotFoundException;

    AccountDto getAccountById(Long id);

    ResponseEntity<AccountDto> deposit(TransactionRequest request) throws AccountNotFoundException;

    ResponseEntity<AccountDto> withdraw(TransactionRequest request) throws AccountNotFoundException;
}



