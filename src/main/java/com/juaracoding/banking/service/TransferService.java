package com.juaracoding.banking.service;

import com.juaracoding.banking.model.Account;
import com.juaracoding.banking.model.TransferRequest;
import com.juaracoding.banking.model.TransferResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service untuk memproses transaksi transfer dana pada Core Banking.
 */
public class TransferService {

    public static final double MAX_SINGLE_TRANSFER_LIMIT = 25_000_000.0; // Rp 25.000.000
    private static final AtomicInteger TRX_COUNTER = new AtomicInteger(1001);

    private final Map<String, Account> accountRepository = new HashMap<>();

    public TransferService() {
        initDummyAccounts();
    }

    public void initDummyAccounts() {
        accountRepository.clear();
        accountRepository.put("1122334455", new Account("1122334455", "Budi Santoso", 50_000_000.0, "ACTIVE", "IDR"));
        accountRepository.put("9988776655", new Account("9988776655", "Siti Aminah", 10_000_000.0, "ACTIVE", "IDR"));
        accountRepository.put("5544332211", new Account("5544332211", "Rudi Hermawan", 2_000_000.0, "ACTIVE", "IDR"));
        accountRepository.put("7788990011", new Account("7788990011", "Dormant Account", 500_000.0, "BLOCKED", "IDR"));
    }

    public Account getAccount(String accountNumber) {
        return accountRepository.get(accountNumber);
    }

    public boolean isAccountValid(String accountNumber) {
        Account acc = accountRepository.get(accountNumber);
        return acc != null && acc.isActive();
    }

    public TransferResponse executeTransfer(TransferRequest request) {
        Account source = accountRepository.get(request.getSourceAccountNumber());
        Account destination = accountRepository.get(request.getDestinationAccountNumber());

        // 1. Validasi Rekening Sumber
        if (source == null || !source.isActive()) {
            return new TransferResponse(null, 404, "SOURCE_ACCOUNT_INVALID_OR_INACTIVE",
                    request.getSourceAccountNumber(), request.getDestinationAccountNumber(),
                    null, request.getAmount(), 0, 0);
        }

        // 2. Validasi Rekening Tujuan
        if (destination == null || !destination.isActive()) {
            return new TransferResponse(null, 404, "DESTINATION_ACCOUNT_INVALID_OR_INACTIVE",
                    request.getSourceAccountNumber(), request.getDestinationAccountNumber(),
                    null, request.getAmount(), 0, source.getBalance());
        }

        // 3. Validasi Batas Maksimal Transfer Tunggal
        if (request.getAmount() > MAX_SINGLE_TRANSFER_LIMIT) {
            return new TransferResponse(null, 422, "TRANSFER_LIMIT_EXCEEDED",
                    request.getSourceAccountNumber(), request.getDestinationAccountNumber(),
                    destination.getAccountHolder(), request.getAmount(), 0, source.getBalance());
        }

        // 4. Hitung Biaya Admin (Internal = Bebas Biaya Rp 0)
        double adminFee = 0.0;
        double totalDeduction = request.getAmount() + adminFee;

        // 5. Validasi Kecukupan Saldo
        if (source.getBalance() < totalDeduction) {
            return new TransferResponse(null, 400, "INSUFFICIENT_FUNDS",
                    request.getSourceAccountNumber(), request.getDestinationAccountNumber(),
                    destination.getAccountHolder(), request.getAmount(), adminFee, source.getBalance());
        }

        // 6. Mutasi Saldo
        source.setBalance(source.getBalance() - totalDeduction);
        destination.setBalance(destination.getBalance() + request.getAmount());

        // 7. Generate Reference Number
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String refNo = String.format("TRX-%s-%05d", datePart, TRX_COUNTER.getAndIncrement());

        return new TransferResponse(refNo, 200, "SUCCESS",
                request.getSourceAccountNumber(), request.getDestinationAccountNumber(),
                destination.getAccountHolder(), request.getAmount(), adminFee, source.getBalance());
    }
}
