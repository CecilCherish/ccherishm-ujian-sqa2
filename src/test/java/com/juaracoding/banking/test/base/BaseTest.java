package com.juaracoding.banking.test.base;

import com.juaracoding.banking.service.AuthService;
import com.juaracoding.banking.service.TransferService;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * BaseTest induk yang mengelola siklus hidup (lifecycle) lingkungan pengujian TestNG.
 */
public abstract class BaseTest {

    protected AuthService authService;
    protected TransferService transferService;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.out.println("================================================================================");
        System.out.println(" 🚀 [LIFECYCLE] @BeforeSuite: INISIALISASI CORE BANKING TEST SUITE");
        System.out.println(" Environment       : STAGING-JAKARTA-DC");
        System.out.println(" Core Banking API  : v2.4-RELEASE");
        System.out.println(" Database Gateway  : JDBC Mock Pool Active");
        System.out.println(" Execution Time    : " + new Date());
        System.out.println("================================================================================");
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        System.out.println("[LIFECYCLE] @BeforeClass: Menginisialisasi Service Dependencies untuk " + getClass().getSimpleName());
        authService = new AuthService();
        transferService = new TransferService();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println(" 🟢 [TEST START] >> " + method.getName() + "()");
        // Reset state data rekening agar setiap test method terisolasi (idempotent)
        if (transferService != null) {
            transferService.initDummyAccounts();
        }
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();

        if (result.getStatus() == ITestResult.SUCCESS) {
            System.out.printf(" ✅ [TEST FINISH] << %s() : PASSED (%d ms)%n", testName, duration);
        } else if (result.getStatus() == ITestResult.FAILURE) {
            System.err.printf(" ❌ [TEST FINISH] << %s() : FAILED (%d ms)%n", testName, duration);
            if (result.getThrowable() != null) {
                System.err.println("    Reason: " + result.getThrowable().getMessage());
            }
        } else if (result.getStatus() == ITestResult.SKIP) {
            System.out.printf(" ⚠️ [TEST FINISH] << %s() : SKIPPED%n", testName);
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        System.out.println("[LIFECYCLE] @AfterClass: Teardown Resource untuk " + getClass().getSimpleName());
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        System.out.println("\n================================================================================");
        System.out.println(" 🏁 [LIFECYCLE] @AfterSuite: SELURUH TEST SUITE SELESAI DIEKSEKUSI");
        System.out.println(" Laporan HTML Tersedia di: target/surefire-reports/index.html");
        System.out.println("================================================================================");
    }
}
