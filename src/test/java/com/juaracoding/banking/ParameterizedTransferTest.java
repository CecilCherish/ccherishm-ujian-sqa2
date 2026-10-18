package com.juaracoding.banking;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ParameterizedTransferTest {
    @Parameters({"environment", "channel", "requestTimeout"})
    @Test
    public void testChannelExecution(String env, String channel, int timeout) {
        System.out.printf("Inisialisasi Channel: %s di Lingkungan: %s (Timeout: %d ms)%n", channel, env, timeout);
    }
}