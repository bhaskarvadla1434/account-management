package com.banking.account_management.dto;

public class AccountResponse {
    private Long id;
    private Long userId;
    private String type;
    private Double balance;
    private String status;

    public AccountResponse(Long id, Long userId, String type, Double balance, String status) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.balance = balance;
        this.status = status;
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getType() { return type; }
    public Double getBalance() { return balance; }
    public String getStatus() { return status; }
}
