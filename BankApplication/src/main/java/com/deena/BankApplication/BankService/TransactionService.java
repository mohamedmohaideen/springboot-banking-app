package com.deena.BankApplication.BankService;


import com.deena.BankApplication.BankRepository.TransactionRepository;
import com.deena.BankApplication.DTO.TransactionDto;
import com.deena.BankApplication.Entity.Transaction;
import com.deena.BankApplication.Mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Page<TransactionDto> getTransactionsByAccount(Long accountId, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Transaction> txPage = transactionRepository.findByAccountId(accountId, pageable);

        // ✅ Convert to DTO safely
        return txPage.map(TransactionMapper::toDto);
    }
}
