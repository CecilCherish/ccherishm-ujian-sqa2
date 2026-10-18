package com.juaracoding.banking.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Service untuk menangani autentikasi nasabah / teller ke Core Banking.
 */
public class AuthService {

    private final Map<String, String> credentialsDb = new HashMap<>();
    private final Map<String, Boolean> activeSessions = new HashMap<>();

    public AuthService() {
        credentialsDb.put("sdet.lead@bank.com", "Secret@2026");
        credentialsDb.put("nasabah.priority@bank.com", "Priority@2026");
        credentialsDb.put("blocked.user@bank.com", "Secret@2026");
    }

    public String authenticate(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Username dan password tidak boleh kosong!");
        }

        if ("blocked.user@bank.com".equalsIgnoreCase(username)) {
            throw new SecurityException("Akun nasabah terblokir karena indikasi aktivitas mencurigakan!");
        }

        String storedPassword = credentialsDb.get(username);
        if (storedPassword == null || !storedPassword.equals(password)) {
            throw new SecurityException("Kredensial username atau password tidak valid!");
        }

        String sessionToken = "TOKEN-" + UUID.randomUUID();
        activeSessions.put(sessionToken, true);
        return sessionToken;
    }

    public boolean isSessionValid(String token) {
        return token != null && activeSessions.getOrDefault(token, false);
    }

    public void invalidateSession(String token) {
        if (token != null) {
            activeSessions.remove(token);
        }
    }
}
