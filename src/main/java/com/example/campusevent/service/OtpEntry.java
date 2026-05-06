package com.example.campusevent.service;

import java.time.LocalDateTime;

/**
 * Simple POJO holding an OTP value and its expiry timestamp.
 * Not a JPA entity — stored in-memory only.
 */
public class OtpEntry {

    private final String otp;
    private final LocalDateTime expiresAt;

    public OtpEntry(String otp, LocalDateTime expiresAt) {
        this.otp = otp;
        this.expiresAt = expiresAt;
    }

    public String getOtp() { return otp; }
    public LocalDateTime getExpiresAt() { return expiresAt; }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
