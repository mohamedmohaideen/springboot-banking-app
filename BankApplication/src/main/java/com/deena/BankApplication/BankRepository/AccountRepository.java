package com.deena.BankApplication.BankRepository;

import com.deena.BankApplication.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    List<Account> findByCustomerId(Long customerId);
//    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.transactions WHERE a.id = :id")
//    Optional<Account> findByIdWithTransactions(@Param("id") Long id);
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.customer WHERE a.id = :id")
    Optional<Account> findByIdWithCustomer(@Param("id") Long id);
}
