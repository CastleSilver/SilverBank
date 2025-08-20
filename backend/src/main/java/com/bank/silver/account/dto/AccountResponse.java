package com.bank.silver.account.dto;

import com.bank.silver.account.entity.Account;

import java.math.BigDecimal;

public record AccountResponse(String accountNumber, BigDecimal balance) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(account.getAccountNumber(), account.getBalance());
    }
}
