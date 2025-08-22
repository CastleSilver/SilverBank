package com.bank.silver.account.dto.request;

import java.math.BigDecimal;

public record WithdrawRequest(String accountNumber, BigDecimal amount) {
}
