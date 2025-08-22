package com.bank.silver.account.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal balanceAfter;

    private String counterpartyAccountNumber;

    private String counterpartyName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    protected Transaction() {}

    public Transaction(TransactionType type, BigDecimal amount, BigDecimal balanceAfter,
                       String counterpartyAccountNumber, String counterpartyName, Account account) {
        this.type = type;
        this.amount = amount;
        this.account = account;
        this.balanceAfter = balanceAfter;
        this.counterpartyAccountNumber = counterpartyAccountNumber;
        this.counterpartyName = counterpartyName;
        this.createdAt = LocalDateTime.now();
    }
}
