package com.bank.silver.account.entity;

import com.bank.silver.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 12)
    private String accountNumber;

    @Column(nullable = false)
    private Long balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    public Account(String accountNumber, Long balance, User owner) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.owner = owner;
    }
}
