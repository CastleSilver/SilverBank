package com.bank.silver.account.repository;

import com.bank.silver.account.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.account.accountNumber = :accountNumber " +
            "AND YEAR(t.createdAt) = :year " +
            "AND MONTH(t.createdAt) = :month " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountAndMonth(
      @Param("accountNumber") String accountNumber,
      @Param("year") int year,
      @Param("month") int month
    );

}
