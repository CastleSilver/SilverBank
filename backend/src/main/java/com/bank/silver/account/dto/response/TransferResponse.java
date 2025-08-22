package com.bank.silver.account.dto.response;

import com.bank.silver.account.entity.Account;

import java.math.BigDecimal;

public record TransferResponse(
        String fromAccount,
        BigDecimal sentAmount,
        BigDecimal fromBalance,
        String toAccount,
        BigDecimal toBalance) {
    public static TransferResponse from(Account from, Account to, BigDecimal amount) {
        return new TransferResponse(
                from.getAccountNumber(),
                amount,
                from.getBalance(),
                to.getAccountNumber(),
                to.getBalance()
        );
    }
}
