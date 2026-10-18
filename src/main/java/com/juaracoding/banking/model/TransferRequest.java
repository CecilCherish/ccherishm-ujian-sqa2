package com.juaracoding.banking.model;

/**
 * Model data yang merepresentasikan permintaan transfer dana.
 */
public class TransferRequest {

    private final String sourceAccountNumber;
    private final String destinationAccountNumber;
    private final double amount;
    private final String currency;
    private final String description;

    public TransferRequest(String sourceAccountNumber, String destinationAccountNumber, double amount, String currency, String description) {
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }
}
