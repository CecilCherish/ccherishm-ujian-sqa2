package com.juaracoding.banking;

import org.testng.annotations.DataProvider;

public class BankingDataProviders {

    @DataProvider(name = "fraudDetectionMatrix")
    public static Object[][] fraudDetectionMatrix() {
        return new Object[][] {
            // testCaseId, sourceAcc, destAcc, amount, expectedCode, expectedMessage

            // ===== 5 kasus wajib dari soal =====
            {"TC_TRF_007", "1122334455", "9988776655",  5_000_000.0, 200, "SUCCESS"},
            {"TC_TRF_008", "1122334455", "9988776655", 25_000_000.0, 200, "SUCCESS"},
            {"TC_TRF_009", "1122334455", "9988776655", 30_000_000.0, 422, "TRANSFER_LIMIT_EXCEEDED"},
            {"TC_TRF_010", "5544332211", "9988776655", 10_000_000.0, 400, "INSUFFICIENT_FUNDS"},
            {"TC_TRF_011", "1122334455", "7788990011",    100_000.0, 404, "DESTINATION_ACCOUNT_INVALID_OR_INACTIVE"},

            // ===== Kasus tambahan =====
            {"TC_TRF_012", "1122334455", "9988776655", 25_000_001.0, 422, "TRANSFER_LIMIT_EXCEEDED"},
            {"TC_TRF_013", "5544332211", "9988776655",  2_000_000.0, 200, "SUCCESS"},
            {"TC_TRF_014", "5544332211", "9988776655",  2_000_001.0, 400, "INSUFFICIENT_FUNDS"},
            {"TC_TRF_015", "7788990011", "9988776655",    100_000.0, 404, "SOURCE_ACCOUNT_INVALID_OR_INACTIVE"},
            {"TC_TRF_016", "0000000000", "9988776655",    100_000.0, 404, "SOURCE_ACCOUNT_INVALID_OR_INACTIVE"},
            {"TC_TRF_017", "1122334455", "1234567890",    100_000.0, 404, "DESTINATION_ACCOUNT_INVALID_OR_INACTIVE"}
        };
    }
}