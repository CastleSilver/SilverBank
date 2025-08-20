package com.bank.silver.account.dto;

import java.math.BigDecimal;

public record TransferRequest(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
}
