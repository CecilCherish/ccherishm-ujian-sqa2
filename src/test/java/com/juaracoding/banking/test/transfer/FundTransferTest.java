package com.juaracoding.banking.test.transfer;

import com.juaracoding.banking.model.TransferRequest;
import com.juaracoding.banking.model.TransferResponse;
import com.juaracoding.banking.test.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Test class untuk memvalidasi alur transfer dana, batasan limit, dan rincian bukti transfer.
 */
public class FundTransferTest extends BaseTest {

    private String lastGeneratedReferenceNumber;

    @Test(priority = 1, groups = {"regression", "transfer"}, description = "Verifikasi inkuiri rekening tujuan aktif sebelum transfer")
    public void testAccountInquiry() {
        String destinationAcc = "9988776655";

        // Hard Assert: Jika rekening tujuan tidak aktif, transfer tidak boleh dilanjutkan
        boolean isValid = transferService.isAccountValid(destinationAcc);
        Assert.assertTrue(isValid, "Rekening tujuan 9988776655 harus berstatus ACTIVE!");
    }

    // test pertama gagal / sebelum ini, maka test ini gabakal dijalannin karena "dependsOnMethods"
    @Test(priority = 2, groups = {"regression", "transfer"}, dependsOnMethods = {"testAccountInquiry"},
          description = "Transfer dana normal dalam batas limit (Rp 10.000.000)")
    public void testTransferWithinSingleLimit() {
        TransferRequest request = new TransferRequest(
                "1122334455",
                "9988776655",
                10_000_000.0,
                "IDR",
                "Pembayaran Proyek SQA 2026"
        );

        TransferResponse response = transferService.executeTransfer(request);

        // 1. Hard Assert: Gatekeeper Check status transaksi harus 200 SUCCESS
        Assert.assertEquals(response.getStatusCode(), 200, "Status code transfer harus 200 OK!");
        Assert.assertEquals(response.getStatusMessage(), "SUCCESS", "Pesan status harus SUCCESS");

        // Simpan nomor referensi untuk test method turunan
        this.lastGeneratedReferenceNumber = response.getReferenceNumber();

        // 2. Soft Assert: Validasi seluruh rincian slip transaksi secara menyeluruh
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotNull(response.getReferenceNumber(), "Nomor referensi tidak boleh null");
        softAssert.assertTrue(response.getReferenceNumber().startsWith("TRX-"), "Nomor referensi harus diawali 'TRX-'");
        softAssert.assertEquals(response.getSourceAccountNumber(), "1122334455", "Rekening sumber keliru");
        softAssert.assertEquals(response.getDestinationAccountNumber(), "9988776655", "Rekening tujuan keliru");
        softAssert.assertEquals(response.getDestinationHolderName(), "Siti Aminah", "Nama pemilik rekening tujuan salah");
        softAssert.assertEquals(response.getTransferAmount(), 10_000_000.0, "Nominal transfer salah");
        softAssert.assertEquals(response.getAdminFee(), 0.0, "Biaya admin transfer internal harus Rp 0");
        softAssert.assertEquals(response.getFinalBalance(), 40_000_000.0, "Sisa saldo pengirim tidak sesuai (50jt - 10jt = 40jt)");

        // Evaluasi seluruh assertion di akhir
        softAssert.assertAll();
    }

    @Test(priority = 3, groups = {"regression", "transfer"}, description = "Transfer melebihi limit per transaksi (> Rp 25.000.000) harus ditolak") // catatan: enabled = true artinya dinonaktifkan
    public void testTransferExceedSingleLimit() {
        TransferRequest request = new TransferRequest(
                "1122334455",
                "9988776655",
                30_000_000.0, // Melebihi batas tunggal Rp 25jt
                "IDR",
                "Beli Kendaraan Operasional"
        );

        TransferResponse response = transferService.executeTransfer(request);

        // Hard Assert: Sistem harus menolak dengan status 422
        Assert.assertEquals(response.getStatusCode(), 422, "Status code harus 422 UNPROCESSABLE_ENTITY");
        Assert.assertEquals(response.getStatusMessage(), "TRANSFER_LIMIT_EXCEEDED", "Pesan penolakan harus TRANSFER_LIMIT_EXCEEDED");
    }

    @Test(priority = 4, enabled = false,
          description = "Fitur transfer valuta asing SGD ke DBS Singapore (Sedang Maintenance)")
    public void testForexTransferSGD() {
        // Method ini akan di-skip oleh TestNG karena gateway BI-FAST FX sedang dalam pengembangan
        System.out.println("Eksekusi transfer valuta asing SGD...");
    }

    @Test(priority = 5, groups = {"regression", "transfer"}, dependsOnMethods = {"testTransferWithinSingleLimit"},
          description = "Download dan verifikasi nomor resi bukti transfer yang sah")
    public void testDownloadTransferReceipt() {
        Assert.assertNotNull(lastGeneratedReferenceNumber, "Nomor referensi transaksi harus sudah tersedia dari Test 2");
        Assert.assertTrue(lastGeneratedReferenceNumber.length() >= 15, "Panjang nomor referensi harus minimal 15 karakter");
        System.out.println("Resi transfer berhasil di-generate dengan No Ref: " + lastGeneratedReferenceNumber);
    }

    @Test(priority = 6, groups = {"regression", "transfer"},description = "Transfer ditolak jika saldo rekening sumber tidak mencukupi")
    public void testTransferInsufficientFunds() {
        TransferRequest request = new TransferRequest(
                "5544332211", // Saldo hanya Rp 2.000.000
                "9988776655",
                5_000_000.0,  // Meminta Rp 5.000.000
                "IDR",
                "Transfer Dana Melebihi Saldo"
        );

        TransferResponse response = transferService.executeTransfer(request);

        // Hard Assert: Sistem harus menolak dengan status 400
        Assert.assertEquals(response.getStatusCode(), 400, "Status code harus 400 BAD_REQUEST");
        Assert.assertEquals(response.getStatusMessage(), "INSUFFICIENT_FUNDS", "Pesan penolakan harus INSUFFICIENT_FUNDS");
    }
}
