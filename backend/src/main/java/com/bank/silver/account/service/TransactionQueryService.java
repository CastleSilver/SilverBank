package com.bank.silver.account.service;

import com.bank.silver.account.dto.response.MonthlyTransactionResponse;
import com.bank.silver.account.entity.Transaction;
import com.bank.silver.account.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionQueryService {

    private final TransactionRepository transactionRepository;

    public MonthlyTransactionResponse getMonthlyTransactions(String accountNumber, int year, int month) {
        List<Transaction> transactions = transactionRepository.findByAccountAndMonth(accountNumber, year, month);
        return MonthlyTransactionResponse.from(accountNumber, year, month, transactions);
    }
}
