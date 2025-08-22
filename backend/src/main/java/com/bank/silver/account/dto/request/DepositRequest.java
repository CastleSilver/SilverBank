package com.bank.silver.account.dto.request;

import java.math.BigDecimal;

public record DepositRequest(String accountNumber, BigDecimal amount) {
}
