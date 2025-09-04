package com.banking.account_management.dto;

public class AccountRequest {
    private Long userId;
    private String type;
    private Double initialBalance;

    public AccountRequest() {}
    public AccountRequest(Long userId, String type, Double initialBalance) {
        this.userId = userId;
        this.type = type;
        this.initialBalance = initialBalance;
    }	

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getInitialBalance() { return initialBalance; }
    public void setInitialBalance(Double initialBalance) { this.initialBalance = initialBalance; }
}
