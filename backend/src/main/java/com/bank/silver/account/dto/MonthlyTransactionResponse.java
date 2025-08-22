package com.bank.silver.account.dto;

import com.bank.silver.account.entity.Account;
import com.bank.silver.account.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

public record MonthlyTransactionResponse(
        String accountNumber,
        BigDecimal balance,
        int year,
        int month,
        List<AccountDetailResponse.TransactionDto> transactions
) {
    public static MonthlyTransactionResponse from(Account account, int year, int month, List<Transaction> txs) {
        return new MonthlyTransactionResponse(
                account.getAccountNumber(),
                account.getBalance(),
                year,
                month,
                txs.stream().map(AccountDetailResponse.TransactionDto::from).toList()
        );
    }
}
