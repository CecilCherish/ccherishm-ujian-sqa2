package com.juaracoding.banking.model;

/**
 * Model data yang merepresentasikan respons transaksi transfer dari Core Banking.
 */
public class TransferResponse {

    private final String referenceNumber;
    private final int statusCode;
    private final String statusMessage;
    private final String sourceAccountNumber;
    private final String destinationAccountNumber;
    private final String destinationHolderName;
    private final double transferAmount;
    private final double adminFee;
    private final double finalBalance;
    private final long timestamp;

    public TransferResponse(String referenceNumber, int statusCode, String statusMessage,
                            String sourceAccountNumber, String destinationAccountNumber,
                            String destinationHolderName, double transferAmount,
                            double adminFee, double finalBalance) {
        this.referenceNumber = referenceNumber;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.destinationHolderName = destinationHolderName;
        this.transferAmount = transferAmount;
        this.adminFee = adminFee;
        this.finalBalance = finalBalance;
        this.timestamp = System.currentTimeMillis();
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public String getDestinationHolderName() {
        return destinationHolderName;
    }

    public double getTransferAmount() {
        return transferAmount;
    }

    public double getAdminFee() {
        return adminFee;
    }

    public double getFinalBalance() {
        return finalBalance;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isSuccess() {
        return statusCode == 200;
    }
}
