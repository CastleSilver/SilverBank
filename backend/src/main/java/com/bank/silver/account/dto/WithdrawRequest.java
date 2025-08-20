package com.bank.silver.account.dto;

import java.math.BigDecimal;

public record WithdrawRequest(String accountNumber, BigDecimal amount) {
}
