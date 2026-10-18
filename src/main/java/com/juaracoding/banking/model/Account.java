package com.juaracoding.banking.model;

/**
 * Model data yang merepresentasikan Rekening Nasabah pada Core Banking.
 */
public class Account {

    private final String accountNumber;
    private final String accountHolder;
    private double balance;
    private String status; // ACTIVE, BLOCKED, SUSPENDED
    private final String currency;

    public Account(String accountNumber, String accountHolder, double balance, String status, String currency) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
        this.status = status;
        this.currency = currency;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }
}
