package com.deena.BankApplication.BankService;

import com.deena.BankApplication.BankRepository.AccountRepository;
import com.deena.BankApplication.BankRepository.CustomerRepository;
import com.deena.BankApplication.BankRepository.TransactionRepository;
import com.deena.BankApplication.DTO.AccountDto;
import com.deena.BankApplication.DTO.MailBody;
import com.deena.BankApplication.DTO.TransactionRequest;
import com.deena.BankApplication.Entity.Account;
import com.deena.BankApplication.Entity.Customer;
import com.deena.BankApplication.Entity.Transaction;
import com.deena.BankApplication.Exception.AccountNotFoundException;
import com.deena.BankApplication.Exception.BadRequestException;
import com.deena.BankApplication.Mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImp implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MailService mailService;

    @Transactional
    @Override
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accounts = accountRepository.findAll()
                .stream()
                .map(AccountMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accounts);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountDto> getAllAccounts(Pageable pageable) {
        Page<Account> accountsPage = accountRepository.findAll(pageable);
        List<AccountDto> dtos = accountsPage.getContent()
                .stream()
                .map(AccountMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, accountsPage.getTotalElements());
    }

    @Override
    @Transactional
    public ResponseEntity<Page<AccountDto>> getAccountsPaginated(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AccountDto> accountsPage = getAllAccounts(pageable);
        return ResponseEntity.ok(accountsPage);
    }

    public ResponseEntity<AccountDto> createAccount(AccountDto dto) {
        Account account = AccountMapper.toEntity(dto);

        if(dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            account.setCustomer(customer);
        }

        Account savedAccount = accountRepository.save(account);
        return ResponseEntity.ok(AccountMapper.toDto(savedAccount));
    }

    @Override
    @Transactional
    public ResponseEntity<AccountDto> getById(Long id) throws AccountNotFoundException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        return ResponseEntity.ok(AccountMapper.toDto(account));
    }

    @Transactional
    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        return AccountMapper.toDto(account);
    }

    @Override
    @Transactional
    public ResponseEntity<AccountDto> deposit(TransactionRequest request) throws AccountNotFoundException {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        // update balance
        account.setBalance(account.getBalance() + request.getAmount());

        // create transaction
        Transaction tx = Transaction.builder()
                .amount(request.getAmount())
                .type("DEPOSIT")
                .timestamp(LocalDateTime.now())
                .account(account) // very important!
                .build();

        // add tx to account's list
        account.getTransactions().add(tx);

        // save account (cascade saves tx also)
        accountRepository.save(account);

        // Send Email after deposit
        MailBody mailBody = MailBody.builder()
                .to(new String[]{account.getCustomer().getEmail()})
                .subject("Deposit Successful")
                .text("Dear " + account.getHolderName() + ",\n\n" +
                        "Your account has been credited with Rs." + request.getAmount() +
                        ".\nCurrent balance: " + account.getBalance())
                .sentDate(new Date())
                .build();

        mailService.sendMessage(mailBody);

        return ResponseEntity.ok(AccountMapper.toDto(account));
    }


    @Override
    @Transactional
    public ResponseEntity<AccountDto> withdraw(TransactionRequest request) throws AccountNotFoundException {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (account.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance!");
        }

        // update balance
        account.setBalance(account.getBalance() - request.getAmount());

        // create transaction
        Transaction tx = Transaction.builder()
                .amount(request.getAmount())
                .type("WITHDRAW")
                .timestamp(LocalDateTime.now())
                .account(account) // link back
                .build();

        // add tx to account's list
        account.getTransactions().add(tx);

        // save account (cascades save transaction too)
        accountRepository.save(account);

        // Send Email
        MailBody mailBody = MailBody.builder()
                .to(new String[]{account.getCustomer().getEmail()})
                .subject("Withdrawal Successful")
                .text("Dear " + account.getHolderName() + ",\n\n" +
                        "Your account has been debited with Rs." + request.getAmount() +
                        ".\nCurrent balance: " + account.getBalance())
                .sentDate(new Date())
                .build();

        mailService.sendMessage(mailBody);

        return ResponseEntity.ok(AccountMapper.toDto(account));
    }

}

