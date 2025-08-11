package com.bank.silver.account.service;

import com.bank.silver.account.entity.Account;
import com.bank.silver.account.repository.AccountRepository;
import com.bank.silver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(User owner) {
        String accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, 0L, owner);
        return accountRepository.save(account);
    }

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
