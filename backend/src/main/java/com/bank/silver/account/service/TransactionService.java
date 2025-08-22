package com.bank.silver.account.service;

import com.bank.silver.account.entity.Account;
import com.bank.silver.account.entity.Transaction;
import com.bank.silver.account.entity.TransactionType;
import com.bank.silver.account.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public void recordTransaction(Account account, BigDecimal amount, TransactionType type,
                                  String counterpartyAccountNumber, String counterpartyName) {
        Transaction tx = new Transaction(type, amount, account.getBalance(),
                counterpartyAccountNumber, counterpartyName, account);
        transactionRepository.save(tx);
    }
}
