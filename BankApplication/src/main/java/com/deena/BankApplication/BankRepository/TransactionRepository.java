package com.deena.BankApplication.BankRepository;

import com.deena.BankApplication.Entity.Transaction;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId);

    Page<Transaction> findByAccountId(Long accountId, Pageable pageable);
}
