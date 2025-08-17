package com.bank.silver.account.repository;

import com.bank.silver.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String generatedNumber);

    Optional<Account> findByAccountNumber(String accountNumber);
}
