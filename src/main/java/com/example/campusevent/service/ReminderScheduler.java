package com.example.campusevent.service;

import com.example.campusevent.entity.Event;
import com.example.campusevent.entity.Registration;
import com.example.campusevent.repository.EventRepository;
import com.example.campusevent.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Scheduled job that sends reminder emails to registered students
 * 24 hours before their event.
 *
 * Runs daily at 8:00 AM server time.
 * @EnableScheduling is added to CampusEventApplication.
 */
@Component
public class ReminderScheduler {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Runs daily at 8 AM. Finds events happening tomorrow and sends reminders.
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void sendReminders() {
        LocalDateTime tomorrowStart = LocalDate.now().plusDays(1).atStartOfDay();
        LocalDateTime tomorrowEnd   = tomorrowStart.plusDays(1).minusSeconds(1);

        List<Event> tomorrowEvents = eventRepository.findByDateRange(tomorrowStart, tomorrowEnd);

        if (tomorrowEvents.isEmpty()) {
            System.out.println("[ReminderScheduler] No events tomorrow — nothing to send.");
            return;
        }

        int emailsSent = 0;
        for (Event event : tomorrowEvents) {
            List<Registration> registrations =
                    registrationRepository.findByEventIdOrderByRegisteredAtDesc(event.getId());

            for (Registration reg : registrations) {
                try {
                    emailService.sendReminderEmail(
                            reg.getEmail(),
                            reg.getStudentName(),
                            event.getTitle(),
                            event.getEventDate().format(FMT),
                            event.getVenue()
                    );
                    emailsSent++;
                } catch (Exception e) {
                    System.err.println("[ReminderScheduler] Failed to send reminder to "
                            + reg.getEmail() + ": " + e.getMessage());
                }
            }
        }
        System.out.println("[ReminderScheduler] Sent " + emailsSent
                + " reminder email(s) for " + tomorrowEvents.size() + " event(s).");
    }
}
