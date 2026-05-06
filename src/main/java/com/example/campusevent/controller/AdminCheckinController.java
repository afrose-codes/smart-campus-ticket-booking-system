package com.example.campusevent.controller;

import com.example.campusevent.entity.Registration;
import com.example.campusevent.repository.RegistrationRepository;
import com.example.campusevent.service.ReminderScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Admin check-in: verify a student's registration by ID.
 * GET  /admin/checkin?regId={id}
 * POST /admin/test-reminder  — manually trigger the reminder scheduler
 */
@Controller
@RequestMapping("/admin")
public class AdminCheckinController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ReminderScheduler reminderScheduler;

    @GetMapping("/checkin")
    public String checkin(@RequestParam(required = false) Long regId, Model model) {
        if (regId != null) {
            Registration reg = registrationRepository.findById(regId).orElse(null);
            if (reg != null) {
                model.addAttribute("registration", reg);
                model.addAttribute("found", true);
            } else {
                model.addAttribute("found", false);
                model.addAttribute("regId", regId);
            }
        }
        return "admin-checkin";
    }

    /** POST /admin/test-reminder — manually trigger the reminder scheduler for testing. */
    @PostMapping("/test-reminder")
    @ResponseBody
    public ResponseEntity<Map<String, String>> testReminder() {
        try {
            reminderScheduler.sendReminders();
            return ResponseEntity.ok(Map.of("status",
                    "Reminder job executed. Check server console for output."));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("status", "Error: " + e.getMessage()));
        }
    }

    /** GET /admin/test-reminders — browser-friendly test trigger. */
    @GetMapping("/test-reminders")
    @ResponseBody
    public String testRemindersGet() {
        try {
            reminderScheduler.sendReminders();
            return "Reminder scheduler triggered! Check Spring Boot console logs.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
