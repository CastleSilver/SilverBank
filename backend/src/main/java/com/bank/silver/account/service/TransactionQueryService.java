package com.bank.silver.account.service;

import com.bank.silver.account.dto.MonthlyTransactionResponse;
import com.bank.silver.account.entity.Account;
import com.bank.silver.account.entity.Transaction;
import com.bank.silver.account.repository.AccountRepository;
import com.bank.silver.account.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionQueryService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public MonthlyTransactionResponse getMonthlyTransactions(String accountNumber, int year, int month) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        List<Transaction> transactions = transactionRepository.findByAccountAndMonth(accountNumber, year, month);
        return MonthlyTransactionResponse.from(account, year, month, transactions);
    }
}
