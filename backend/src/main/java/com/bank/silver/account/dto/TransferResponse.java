package com.bank.silver.account.dto;

import com.bank.silver.account.entity.Account;

import java.math.BigDecimal;

public record TransferResponse(String fromAccount, BigDecimal fromBalance,
                               String toAccount, BigDecimal toBalance) {
    public static TransferResponse from(Account from, Account to) {
        return new TransferResponse(from.getAccountNumber(), from.getBalance(),
                to.getAccountNumber(), to.getBalance());
    }
}
