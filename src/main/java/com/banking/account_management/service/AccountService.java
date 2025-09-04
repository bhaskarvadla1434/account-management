package com.banking.account_management.service;

import com.banking.account_management.dto.AccountRequest;
import com.banking.account_management.dto.AccountResponse;
import com.banking.account_management.model.Account;
import com.banking.account_management.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  // ✅ SLF4J
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class); // ✅ Logger

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Create
    public AccountResponse createAccount(AccountRequest request) {
        log.info("Creating new account for userId={}, type={}, initialBalance={}",
                request.getUserId(), request.getType(), request.getInitialBalance());

        if (request.getInitialBalance() == null || request.getInitialBalance() < 0) {
            log.error("Invalid initial balance: {}", request.getInitialBalance());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid initial balance");
        }

        Account account = new Account();
        account.setUserId(request.getUserId());
        account.setType(request.getType());
        account.setBalance(request.getInitialBalance());
        account.setStatus("Active");

        Account savedAccount = accountRepository.save(account);

        log.debug("Account created successfully: {}", savedAccount.getId());
        return mapToResponse(savedAccount);
    }

    // Read by ID
    public AccountResponse getAccountById(Long id) {
        log.info("Fetching account with id={}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Account not found: id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
                });
        return mapToResponse(account);
    }

    // Read All
    public List<AccountResponse> getAllAccounts() {
        log.info("Fetching all accounts");
        List<AccountResponse> accounts = accountRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        log.debug("Total accounts found: {}", accounts.size());
        return accounts;
    }

    // Update Account Details
    public AccountResponse updateAccount(Long id, AccountRequest request) {
        log.info("Updating account id={}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Account not found: id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
                });

        if (request.getType() != null) account.setType(request.getType());
        if (request.getInitialBalance() != null) account.setBalance(request.getInitialBalance());

        Account updated = accountRepository.save(account);
        log.debug("Account updated: id={}, type={}, balance={}", updated.getId(), updated.getType(), updated.getBalance());
        return mapToResponse(updated);
    }

    // Update Balance
    public AccountResponse updateBalance(Long id, Double amount, String operation) {
        log.info("Updating balance for account id={}, operation={}, amount={}", id, operation, amount);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Account not found for update: id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
                });

        if ("deposit".equalsIgnoreCase(operation)) {
            account.setBalance(account.getBalance() + amount);
            log.debug("Deposited {} to account id={}. New balance={}", amount, id, account.getBalance());
        } else if ("withdraw".equalsIgnoreCase(operation)) {
            if (account.getBalance() < amount) {
                log.error("Insufficient funds for withdrawal: id={}, balance={}, requested={}",
                        id, account.getBalance(), amount);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
            }
            account.setBalance(account.getBalance() - amount);
            log.debug("Withdrew {} from account id={}. New balance={}", amount, id, account.getBalance());
        } else {
            log.error("Invalid operation: {}", operation);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation");
        }

        Account updated = accountRepository.save(account);
        return mapToResponse(updated);
    }

    // Delete
    public void deleteAccount(Long id) {
        log.info("Deleting account id={}", id);
        if (!accountRepository.existsById(id)) {
            log.warn("Account not found for deletion: id={}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        accountRepository.deleteById(id);
        log.debug("Account deleted: id={}", id);
    }

    // Mapper
    private AccountResponse mapToResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getUserId(),
                account.getType(),
                account.getBalance(),
                account.getStatus()
        );
    }
}
