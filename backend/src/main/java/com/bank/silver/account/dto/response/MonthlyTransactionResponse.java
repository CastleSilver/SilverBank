package com.bank.silver.account.dto.response;

import com.bank.silver.account.entity.Transaction;

import java.util.Comparator;
import java.util.List;

public record MonthlyTransactionResponse(
        String accountNumber,
        int year,
        int month,
        List<AccountDetailResponse.TransactionDto> transactions
) {
    public static MonthlyTransactionResponse from(String accountNumber, int year, int month, List<Transaction> txs) {
        return new MonthlyTransactionResponse(
                accountNumber,
                year,
                month,
                txs.stream().map(AccountDetailResponse.TransactionDto::from)
                        .sorted(Comparator.comparing(AccountDetailResponse.TransactionDto::createdAt)).toList()
        );
    }
}
