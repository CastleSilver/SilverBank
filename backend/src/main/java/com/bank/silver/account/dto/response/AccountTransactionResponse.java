package com.bank.silver.account.dto.response;

import com.bank.silver.account.entity.Account;

import java.math.BigDecimal;

public record AccountTransactionResponse(
        String accountNumber,
        BigDecimal amount,
        BigDecimal balance) {
    public static AccountTransactionResponse from(Account account, BigDecimal amount) {
        return new AccountTransactionResponse(
                account.getAccountNumber(),
                amount,
                account.getBalance());
    }
}
