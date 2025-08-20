package com.bank.silver.account.dto;

import java.math.BigDecimal;

public record DepositRequest(String accountNumber, BigDecimal amount) {
}
