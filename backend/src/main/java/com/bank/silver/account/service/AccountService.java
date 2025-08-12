package com.bank.silver.account.service;

import com.bank.silver.account.entity.Account;
import com.bank.silver.account.repository.AccountRepository;
import com.bank.silver.account.util.AccountNumberGenerator;
import com.bank.silver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Account account = new Account(accountNumber, 0L, owner);
        return accountRepository.save(account);
    }
}
