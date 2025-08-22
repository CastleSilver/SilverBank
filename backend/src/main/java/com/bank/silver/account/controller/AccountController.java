package com.bank.silver.account.controller;

import com.bank.silver.account.dto.request.DepositRequest;
import com.bank.silver.account.dto.request.TransferRequest;
import com.bank.silver.account.dto.request.WithdrawRequest;
import com.bank.silver.account.dto.response.AccountDetailResponse;
import com.bank.silver.account.dto.response.AccountTransactionResponse;
import com.bank.silver.account.dto.response.MonthlyTransactionResponse;
import com.bank.silver.account.dto.response.TransferResponse;
import com.bank.silver.account.entity.Account;
import com.bank.silver.account.service.AccountService;
import com.bank.silver.account.service.TransactionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final  AccountService accountService;
    private final TransactionQueryService transactionQueryService;

    @PostMapping("/deposit")
    public ResponseEntity<AccountTransactionResponse> deposit(@RequestBody DepositRequest depositRequest) {
        return ResponseEntity.ok(accountService.deposit(
                depositRequest.accountNumber(),
                depositRequest.amount()
        ));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountTransactionResponse> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        return ResponseEntity.ok(accountService.withdraw(
                withdrawRequest.accountNumber(),
                withdrawRequest.amount()
        ));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest transferRequest) {
        return ResponseEntity.ok(accountService.transfer(
                transferRequest.fromAccountNumber(),
                transferRequest.toAccountNumber(),
                transferRequest.amount()
        ));
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
