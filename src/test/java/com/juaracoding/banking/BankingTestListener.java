package com.juaracoding.banking;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

public class BankingTestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("\n🚀 [LISTENER] Memulai Test: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.printf(" ▶ [LISTENER] Eksekusi: %s#%s()%n",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
        if (result.getParameters().length > 0) {
            System.out.println("   Data uji: " + Arrays.toString(result.getParameters()));
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        System.out.printf(" ✓ [LISTENER] Skenario '%s' PASSED dalam %d ms.%n",
                result.getMethod().getMethodName(), duration);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.err.printf(" ✖ [LISTENER] DEFECT TERDETEKSI pada: %s%n",
                result.getMethod().getMethodName());
        System.out.println(" Penyebab Kegagalan: " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.printf(" ⚡ [LISTENER] Skenario '%s' SKIPPED.%n",
                result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        int total = passed + failed + skipped;
        double passRate = total == 0 ? 0 : ((double) passed / total) * 100;

        System.out.println("\n========================================================");
        System.out.println(" 💾 RINGKASAN TELEMETRI: " + context.getName());
        System.out.println("========================================================");
        System.out.printf(" Total Test Skenario : %d%n", total);
        System.out.printf(" Passed              : %d%n", passed);
        System.out.printf(" Failed              : %d%n", failed);
        System.out.printf(" Skipped             : %d%n", skipped);
        System.out.printf(" Pass Rate           : %.2f%%%n", passRate);
        System.out.println("========================================================\n");
    }
}