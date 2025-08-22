package com.bank.silver.account.controller;

import com.bank.silver.account.dto.*;
import com.bank.silver.account.entity.Account;
import com.bank.silver.account.service.AccountService;
import com.bank.silver.account.service.TransactionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionQueryService transactionQueryService;

    @PostMapping("/deposit")
    public ResponseEntity<AccountResponse> deposit(@RequestBody DepositRequest depositRequest) {
        Account account = accountService.deposit(depositRequest.accountNumber(), depositRequest.amount());
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountResponse> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        Account account = accountService.withdraw(withdrawRequest.accountNumber(), withdrawRequest.amount());
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest transferRequest) {
        Account[] accounts = accountService.transfer(transferRequest.fromAccountNumber(), transferRequest.toAccountNumber(), transferRequest.amount());
        return ResponseEntity.ok(TransferResponse.from(accounts[0], accounts[1]));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDetailResponse> getAccountDetail(@PathVariable String accountNumber) {
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(AccountDetailResponse.from(account));
    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<MonthlyTransactionResponse> getMonthlyTransactions(
            @PathVariable String accountNumber,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(transactionQueryService.getMonthlyTransactions(accountNumber, year, month));
    }
}
