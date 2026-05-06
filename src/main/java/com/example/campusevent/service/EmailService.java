package com.example.campusevent.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Sends emails via JavaMailSender (Gmail SMTP).
 * Used by OTP verification, registration confirmation, and the reminder scheduler.
 *
 * IMPORTANT: sendOtpEmail() now THROWS on failure so OtpController can catch it
 * and still return success=true (OTP is stored in memory regardless).
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@smartcampus.edu}")
    private String fromAddress;

    // ─── OTP ────────────────────────────────────────────────────────────────

    /**
     * Sends a 6-digit OTP email.
     * THROWS RuntimeException on failure — caller decides how to handle.
     */
    public void sendOtpEmail(String toEmail, String otp) {
        String subject = "SmartCampus – Your OTP for Event Registration";
        String body = "Dear Student,\n\n"
                + "Your OTP for SmartCampus event registration is:\n\n"
                + "        " + otp + "\n\n"
                + "Valid for 5 minutes. Do not share with anyone.\n\n"
                + "Best regards,\nSmartCampus Team";
        sendPlainEmail(toEmail, subject, body);
    }

    // ─── Registration Confirmation ───────────────────────────────────────────

    /**
     * Sends a registration confirmation email after successful event registration.
     * Silently logs on failure — does not crash the registration flow.
     */
    public void sendRegistrationConfirmation(String toEmail, String studentName,
                                              String eventName, String eventDate, String venue) {
        try {
            String subject = "SmartCampus – Registration Confirmed: " + eventName;
            String body = "Dear " + studentName + ",\n\n"
                    + "Your registration is confirmed!\n\n"
                    + "Event : " + eventName + "\n"
                    + "Date  : " + eventDate + "\n"
                    + "Venue : " + venue + "\n\n"
                    + "Go to My Registrations to download your QR ticket.\n\n"
                    + "Best regards,\nSmartCampus Team";
            sendPlainEmail(toEmail, subject, body);
        } catch (Exception e) {
            System.err.println("[EmailService] Confirmation email failed for " + toEmail + ": " + e.getMessage());
        }
    }

    // ─── Reminder ────────────────────────────────────────────────────────────

    /**
     * Sends a 24-hour reminder email.
     * Silently logs on failure — does not crash the scheduler.
     */
    public void sendReminderEmail(String toEmail, String studentName,
                                   String eventTitle, String eventDate, String venue) {
        try {
            String subject = "⏰ Reminder: " + eventTitle + " is Tomorrow!";
            String body = "Dear " + studentName + ",\n\n"
                    + "This is a reminder that your event is tomorrow!\n\n"
                    + "Event : " + eventTitle + "\n"
                    + "Date  : " + eventDate + "\n"
                    + "Venue : " + venue + "\n\n"
                    + "Bring your QR ticket from the My Registrations page.\n\n"
                    + "Best regards,\nSmartCampus Team";
            sendPlainEmail(toEmail, subject, body);
        } catch (Exception e) {
            System.err.println("[EmailService] Reminder email failed for " + toEmail + ": " + e.getMessage());
        }
    }

    // ─── Internal helpers ────────────────────────────────────────────────────

    /**
     * Sends a plain-text email. THROWS RuntimeException on any failure.
     */
    private void sendPlainEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromAddress);
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(text);
            mailSender.send(msg);
            System.out.println("[EmailService] Sent to: " + to + " | Subject: " + subject);
        } catch (Exception e) {
            System.err.println("[EmailService] FAILED to send to " + to + ": " + e.getMessage());
            throw new RuntimeException("Email send failed: " + e.getMessage(), e);
        }
    }

    /**
     * Sends an HTML email. THROWS RuntimeException on any failure.
     */
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
            System.out.println("[EmailService] HTML email sent to: " + to);
        } catch (MessagingException e) {
            System.err.println("[EmailService] HTML email FAILED for " + to + ": " + e.getMessage());
            throw new RuntimeException("HTML email send failed: " + e.getMessage(), e);
        }
    }
}
