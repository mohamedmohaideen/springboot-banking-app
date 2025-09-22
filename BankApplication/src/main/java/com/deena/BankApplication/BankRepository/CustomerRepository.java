package com.deena.BankApplication.BankRepository;

import com.deena.BankApplication.Entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    boolean existsByEmail(String email);

    Customer findByEmail(String email);

    @EntityGraph(attributePaths = {"accounts", "accounts.transactions"})
    Optional<Customer> findById(Long id);

    @EntityGraph(attributePaths = {"accounts"})
    Page<Customer> findAll(Pageable pageable);
}
