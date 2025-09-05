package com.banking.account_management.controller;

import com.banking.account_management.dto.AccountRequest;
import com.banking.account_management.dto.AccountResponse;
import com.banking.account_management.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "CRUD operations for bank accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //  Create
    @Operation(
        summary = "Create a new account",
        description = "Creates a new bank account for a user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(201).body(response);
    }

    //  Read by ID
    @Operation(
        summary = "Get account by ID",
        description = "Fetch details of an account by its ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        AccountResponse response = accountService.getAccountById(id);
        return ResponseEntity.ok(response);
    }

    //  Read All
    @Operation(
        summary = "Get all accounts",
        description = "Fetch a list of all accounts",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> response = accountService.getAllAccounts();
        return ResponseEntity.ok(response);
    }

    //  Update Entire Account
    @Operation(
        summary = "Update account details",
        description = "Update account type, balance, or status",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountRequest request) {
        AccountResponse response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }

    //  Partial Update: Balance Only
    @Operation(
        summary = "Update account balance",
        description = "Deposit or withdraw money from an account",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}/balance")
    public ResponseEntity<AccountResponse> updateBalance(
            @PathVariable Long id,
            @RequestBody UpdateBalanceRequest request) {
        AccountResponse response = accountService.updateBalance(id, request.getAmount(), request.getOperation());
        return ResponseEntity.ok(response);
    }

    //  Delete
    @Operation(
        summary = "Delete account",
        description = "Delete an account by its ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    // --- Nested DTO for balance update ---
    public static class UpdateBalanceRequest {
        private Double amount;
        private String operation; // deposit | withdraw

        public UpdateBalanceRequest() {}

        public UpdateBalanceRequest(Double amount, String operation) {
            this.amount = amount;
            this.operation = operation;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }
    }
}
