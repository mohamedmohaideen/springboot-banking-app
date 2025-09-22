package com.deena.BankApplication.Controller;

import com.deena.BankApplication.BankService.AccountService;
import com.deena.BankApplication.DTO.AccountDto;
import com.deena.BankApplication.DTO.TransactionRequest;
import com.deena.BankApplication.Exception.AccountNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Get all accounts
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    // Get paginated accounts
    @GetMapping("/paginated")
    public ResponseEntity<?> getAccountsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return accountService.getAccountsPaginated(page, size, sortBy, sortDir);
    }

    // Create new account
    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto dto) {
        return accountService.createAccount(dto);
    }

    // Get account by ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getById(@PathVariable Long id) throws AccountNotFoundException {
        return accountService.getById(id);
    }

    // Deposit to account
    @PutMapping("/deposit")
    public ResponseEntity<AccountDto> deposit(@RequestBody TransactionRequest request) throws AccountNotFoundException {
        return accountService.deposit(request);
    }

    // Withdraw from account
    @PutMapping("/withdraw")
    public ResponseEntity<AccountDto> withdraw(@RequestBody TransactionRequest request) throws AccountNotFoundException {
        return accountService.withdraw(request);
    }
}
