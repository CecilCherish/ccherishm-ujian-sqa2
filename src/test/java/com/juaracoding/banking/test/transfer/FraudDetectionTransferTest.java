package com.juaracoding.banking.test.transfer;

import com.juaracoding.banking.BankingDataProviders;
import com.juaracoding.banking.model.Account;
import com.juaracoding.banking.model.TransferRequest;
import com.juaracoding.banking.model.TransferResponse;
import com.juaracoding.banking.test.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class FraudDetectionTransferTest extends BaseTest {

    private String channel;
    private String environment;

    @BeforeClass(alwaysRun = true)
    @Parameters({"channel", "environment"})
    public void setUpChannel(String channel, String environment) {
        this.channel = channel;
        this.environment = environment;
        System.out.printf("[PARAM] Channel: %s | Environment: %s%n", channel, environment);
    }

    @Test(groups = {"regression", "transfer"},
          dataProvider = "fraudDetectionMatrix",
          dataProviderClass = BankingDataProviders.class,
          description = "Matriks deteksi fraud dan limit transfer (data-driven)")
    public void testFraudDetectionMatrix(String testCaseId, String sourceAcc, String destAcc,
                                         double amount, int expectedCode, String expectedMessage) {

        System.out.printf("[%s] %s via %s di %s%n", testCaseId, expectedMessage, channel, environment);

        Account source = transferService.getAccount(sourceAcc);
        double initialBalance = (source != null) ? source.getBalance() : 0.0;

        TransferRequest request = new TransferRequest(sourceAcc, destAcc, amount, "IDR", "Fraud Matrix " + testCaseId);
        TransferResponse response = transferService.executeTransfer(request);

        Assert.assertEquals(response.getStatusCode(), expectedCode,
                testCaseId + ": status code tidak sesuai");
        Assert.assertEquals(response.getStatusMessage(), expectedMessage,
                testCaseId + ": status message tidak sesuai");

        if (expectedCode == 200) {
            Assert.assertNotNull(response.getReferenceNumber(), testCaseId + ": nomor referensi tidak boleh null");
            Assert.assertTrue(response.getReferenceNumber().startsWith("TRX-"),
                    testCaseId + ": nomor referensi harus diawali 'TRX-'");
            Assert.assertEquals(response.getFinalBalance(), initialBalance - amount, 0.001,
                    testCaseId + ": sisa saldo pengirim tidak sesuai");
        } else {
            Assert.assertNull(response.getReferenceNumber(),
                    testCaseId + ": transaksi gagal tidak boleh punya nomor referensi");
        }
    }
}