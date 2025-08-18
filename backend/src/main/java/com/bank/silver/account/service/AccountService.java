package com.bank.silver.account.service;

import com.bank.silver.account.entity.Account;
import com.bank.silver.account.repository.AccountRepository;
import com.bank.silver.account.util.AccountNumberGenerator;
import com.bank.silver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountNumberGenerator accountNumberGenerator;


    public Account createAccount(User owner) {
        String accountNumber;
        do {
            accountNumber = accountNumberGenerator.generate();
        } while (accountRepository.existsByAccountNumber(accountNumber));

        Account account = new Account(accountNumber, BigDecimal.ZERO, owner);
        return accountRepository.save(account);
    }

    @Transactional
    public Account deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));


        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        account.deposit(amount);

        return accountRepository.save(account);
    }

    @Transactional
    public Account withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive");
        }

        if (amount.compareTo(account.getBalance()) > 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        account.withdraw(amount);

        return accountRepository.save(account);
    }

    @Transactional
    public void transfer(String sendAccount, String receiveAccount, BigDecimal amount) {
        Account sender = accountRepository.findByAccountNumber(sendAccount)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        Account receiver = accountRepository.findByAccountNumber(receiveAccount)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        sender.withdraw(amount);
        receiver.deposit(amount);

        accountRepository.save(sender);
        accountRepository.save(receiver);
    }
}
