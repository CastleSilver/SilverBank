package com.bank.silver.account.service;

import com.bank.silver.account.entity.Account;
import com.bank.silver.account.entity.TransactionType;
import com.bank.silver.account.repository.AccountRepository;
import com.bank.silver.account.util.AccountNumberGenerator;
import com.bank.silver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountNumberGenerator accountNumberGenerator;

    @Autowired
    private TransactionService transactionService;

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
        transactionService.recordTransaction(account, amount, TransactionType.DEPOSIT);

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
        transactionService.recordTransaction(account, amount, TransactionType.WITHDRAW);

        return accountRepository.save(account);
    }

    @Transactional
    public Account[] transfer(String sendAccountNumber, String receiveAccountNumber, BigDecimal amount) {
        if (sendAccountNumber.equals(receiveAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        Account sender = accountRepository.findByAccountNumber(sendAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        Account receiver = accountRepository.findByAccountNumber(receiveAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        sender.withdraw(amount);
        transactionService.recordTransaction(sender, amount, TransactionType.TRANSFER_OUT);

        receiver.deposit(amount);
        transactionService.recordTransaction(receiver, amount, TransactionType.TRANSFER_IN);

        accountRepository.save(sender);
        accountRepository.save(receiver);

        return new Account[]{sender, receiver};
    }
}
