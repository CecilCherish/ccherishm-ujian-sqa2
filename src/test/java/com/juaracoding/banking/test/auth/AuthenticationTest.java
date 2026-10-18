package com.juaracoding.banking.test.auth;

import com.juaracoding.banking.test.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class untuk memvalidasi alur autentikasi pengguna pada Core Banking.
 */
public class AuthenticationTest extends BaseTest {

    private String validSessionToken;

    @Test(priority = 1, groups = {"smoke", "auth"}, description = "Verifikasi login berhasil dengan kredensial nasabah valid")
    public void testValidUserLogin() {
        validSessionToken = authService.authenticate("sdet.lead@bank.com", "Secret@2026");
        // validSessionToken = authService.authenticate("sdet.lead@bank.id", "Secret@2026"); test salah


        // Hard Assert: Token tidak boleh null dan harus memiliki format standar
        Assert.assertNotNull(validSessionToken, "Session token tidak boleh null!");
        Assert.assertTrue(validSessionToken.startsWith("TOKEN-"), "Format token harus diawali dengan 'TOKEN-'");
        Assert.assertTrue(authService.isSessionValid(validSessionToken), "Sesi token harus berstatus aktif di server!");
    }

    @Test(priority = 2, groups = {"smoke", "auth"}, description = "Verifikasi login gagal jika nasabah berstatus terblokir")
    public void testBlockedUserLogin() {
        Assert.assertThrows(SecurityException.class, () -> {
            authService.authenticate("blocked.user@bank.com", "Secret@2026");
        });
    }

    @Test(priority = 3, groups = {"smoke", "auth"}, description = "Verifikasi login gagal jika password salah")
    public void testInvalidPasswordLogin() {
        Assert.assertThrows(SecurityException.class, () -> {
            authService.authenticate("sdet.lead@bank.com", "WrongPassword123");
        });
    }

    @Test(priority = 4, groups = {"smoke", "auth"}, description = "Verifikasi login gagal jika input username kosong")
    public void testEmptyUsernameLogin() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            authService.authenticate("", "Secret@2026");
        });
    }
}
