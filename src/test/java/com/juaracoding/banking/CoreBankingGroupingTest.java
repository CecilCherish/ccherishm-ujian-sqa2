package com.juaracoding.banking;

import org.testng.annotations.Test;

public class CoreBankingGroupingTest {

    @Test(groups = {"smoke", "auth"})
    public void testCustomerLogin() {
        System.out.println("Validasi login nasabah...");
    }
    
    @Test(groups = {"smoke", "transfer"})
    public void testInstantTransferBiFast() {
        System.out.println("Validasi transfer instan BI-FAST...");
    }
    
    @Test(groups = {"regression", "transfer"})
    public void testScheduledRecurringTransfer() {
        System.out.println("Validasi transfer terjadwal akhir bulan...");
    }
    
    @Test(groups = {"security", "regression"})
    public void testPreventSqlInjectionOnAccountSearch() {
        System.out.println("Validasi input sanitization SQL injection...");
    }

}