package com.example.campusevent.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Generates and verifies 6-digit OTPs stored in memory with a 5-minute TTL.
 */
@Service
public class OtpService {

    private static final int OTP_TTL_MINUTES = 5;
    private final ConcurrentHashMap<String, OtpEntry> otpStore = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    /**
     * Generates a 6-digit OTP for the given email, stores it, and returns it.
     */
    public String generateOtp(String email) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        OtpEntry entry = new OtpEntry(otp, LocalDateTime.now().plusMinutes(OTP_TTL_MINUTES));
        otpStore.put(email.toLowerCase(), entry);
        return otp;
    }

    /**
     * Verifies the OTP for the given email.
     * Returns true only if the OTP matches and has not expired.
     * Removes the entry on successful verification.
     */
    public boolean verifyOtp(String email, String otp) {
        OtpEntry entry = otpStore.get(email.toLowerCase());
        if (entry == null || entry.isExpired()) {
            otpStore.remove(email.toLowerCase());
            return false;
        }
        if (entry.getOtp().equals(otp)) {
            otpStore.remove(email.toLowerCase());
            return true;
        }
        return false;
    }
}
