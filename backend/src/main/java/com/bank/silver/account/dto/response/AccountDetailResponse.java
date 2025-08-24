package com.bank.silver.account.dto.response;

import com.bank.silver.account.entity.Account;
import com.bank.silver.account.entity.Transaction;
import com.bank.silver.account.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public record AccountDetailResponse(
        String accountNumber,
        BigDecimal balance,
        String ownerName,
        String ownerEmail,
        List<TransactionDto> transactions) {
    public static AccountDetailResponse from(Account account) {
        return new AccountDetailResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getOwner().getUsername(),
                account.getOwner().getEmail(),
                account.getTransactions().stream()
                        .sorted(Comparator.comparing(Transaction::getCreatedAt))
                        .limit(5)
                        .map(TransactionDto::from)
                        .toList());
    }

    public record TransactionDto(
            BigDecimal amount,
            BigDecimal balanceAfter,
            String counterpartyAccountNumber,
            String counterpartyName,
            TransactionType type,
            LocalDateTime createdAt
    ) {
        public static TransactionDto from(Transaction transaction) {
            return new TransactionDto(
                    transaction.getAmount(),
                    transaction.getBalanceAfter(),
                    transaction.getCounterpartyAccountNumber(),
                    transaction.getCounterpartyName(),
                    transaction.getType(),
                    transaction.getCreatedAt()
            );
        }
    }
}
