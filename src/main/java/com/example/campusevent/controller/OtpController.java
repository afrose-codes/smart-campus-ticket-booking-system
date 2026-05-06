package com.example.campusevent.controller;

import com.example.campusevent.service.EmailService;
import com.example.campusevent.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST endpoints for OTP-based email verification.
 *
 * POST /api/otp/send   — generates OTP, emails it, returns {success, message}
 * POST /api/otp/verify — verifies OTP, returns {verified, message}
 * GET  /api/otp/test?email=X — browser-friendly test endpoint
 */
@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Email is required"));
        }

        // Generate OTP and store in memory FIRST — always succeeds
        String otp = otpService.generateOtp(email.trim());

        // Attempt email delivery
        try {
            emailService.sendOtpEmail(email.trim(), otp);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "OTP sent to " + email + ". Check your inbox!"));
        } catch (Exception e) {
            // Email failed but OTP is still valid in memory
            // Print to console for dev/demo use
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("  DEV MODE OTP for: " + email);
            System.out.println("  OTP CODE: " + otp);
            System.out.println("  (Email delivery failed — use this code)");
            System.out.println("╚══════════════════════════════════════╝");
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "OTP generated! Email delivery failed — check server console for the OTP code."));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp   = body.get("otp");
        if (email == null || otp == null || email.isBlank() || otp.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("verified", false, "message", "Email and OTP are required"));
        }
        boolean verified = otpService.verifyOtp(email.trim(), otp.trim());
        return ResponseEntity.ok(Map.of(
                "verified", verified,
                "message", verified
                        ? "✅ Email verified successfully!"
                        : "❌ Invalid or expired OTP. Please try again."));
    }

    /**
     * GET /api/otp/test?email=your@email.com
     * Browser-friendly test endpoint — sends a real OTP and confirms delivery.
     */
    @GetMapping("/test")
    public ResponseEntity<String> testOtp(@RequestParam String email) {
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email parameter is required.");
        }
        String otp = otpService.generateOtp(email.trim());
        try {
            emailService.sendOtpEmail(email.trim(), otp);
            return ResponseEntity.ok(
                    "✅ Test OTP sent to " + email + "! Check your inbox.\n"
                    + "Also check Spring Boot console logs for confirmation.");
        } catch (Exception e) {
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("  TEST OTP for: " + email);
            System.out.println("  OTP CODE: " + otp);
            System.out.println("╚══════════════════════════════════════╝");
            return ResponseEntity.ok(
                    "⚠️ Email delivery FAILED for " + email + ".\n"
                    + "Error: " + e.getMessage() + "\n\n"
                    + "OTP is still valid — check Spring Boot console for the code.\n\n"
                    + "To fix email: configure MAIL_USERNAME and MAIL_PASSWORD in application.properties\n"
                    + "with a Gmail App Password (myaccount.google.com → Security → App Passwords).");
        }
    }
}
