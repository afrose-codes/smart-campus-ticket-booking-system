package com.example.campusevent.service;

import com.example.campusevent.entity.Event;
import com.example.campusevent.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Chatbot service — answers student questions using real event data from the DB.
 * Rule-based logic; no external API required.
 */
@Service
public class ChatbotService {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    @Autowired
    private EventRepository eventRepository;

    /**
     * Primary entry point — called by ChatbotController.
     * Accepts {message} and optional {studentEmail}.
     */
    public String processQuery(String message, String studentEmail) {
        return getResponse(message);
    }

    /**
     * Alias entry point — called by the /message endpoint.
     */
    public String getResponse(String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return "Please type a message!";
        }

        // Always fetch real events from DB
        List<Event> events = eventRepository.findAll();
        String lower = userMessage.toLowerCase().trim();

        // ── Greeting ──────────────────────────────────────────────────────
        if (lower.contains("hi") || lower.contains("hello") || lower.contains("hey")
                || lower.contains("good morning") || lower.contains("good afternoon")) {
            return "👋 Hello! I'm your SmartCampus assistant!\n\n"
                    + "I can help you with:\n"
                    + "• Upcoming events list\n"
                    + "• Event venues and timings\n"
                    + "• How to register\n"
                    + "• Seat availability\n"
                    + "• Pricing information\n\n"
                    + "What would you like to know?";
        }

        // ── List / Upcoming events ─────────────────────────────────────────
        if (lower.contains("event") || lower.contains("upcoming") || lower.contains("list")
                || lower.contains("what") || lower.contains("show") || lower.contains("all")) {
            if (events.isEmpty()) {
                return "No upcoming events right now. Check back soon!";
            }
            StringBuilder sb = new StringBuilder("📅 Upcoming Events:\n\n");
            for (Event e : events) {
                sb.append("🔹 ").append(e.getTitle())
                  .append("\n   📅 ").append(e.getEventDate() != null ? e.getEventDate().format(FMT) : "TBD")
                  .append("\n   📍 ").append(e.getVenue())
                  .append("\n   🏷️ ").append(e.getType())
                  .append("\n\n");
            }
            return sb.toString().trim();
        }

        // ── How to register ───────────────────────────────────────────────
        if (lower.contains("register") || lower.contains("how to") || lower.contains("sign up")
                || lower.contains("book") || lower.contains("enroll")) {
            return "✅ To register for an event:\n\n"
                    + "1. Go to the Events page\n"
                    + "2. Click Register on any event\n"
                    + "3. Fill your name, department, and number of tickets\n"
                    + "4. Enter your college email and click Send OTP\n"
                    + "5. Enter the OTP received in your email\n"
                    + "6. Click Confirm Registration\n\n"
                    + "Your QR ticket will be available in My Registrations! 🎫";
        }

        // ── Venue / Location ──────────────────────────────────────────────
        if (lower.contains("venue") || lower.contains("where") || lower.contains("location")
                || lower.contains("place") || lower.contains("address")) {
            if (events.isEmpty()) return "No events found to show venues for.";
            StringBuilder sb = new StringBuilder("📍 Event Venues:\n\n");
            for (Event e : events) {
                sb.append("🔹 ").append(e.getTitle()).append(": ").append(e.getVenue()).append("\n");
            }
            return sb.toString().trim();
        }

        // ── Pricing / Free events ─────────────────────────────────────────
        if (lower.contains("free") || lower.contains("price") || lower.contains("cost")
                || lower.contains("fee") || lower.contains("paid") || lower.contains("money")) {
            if (events.isEmpty()) return "No events found to show pricing for.";
            StringBuilder sb = new StringBuilder("💰 Event Pricing:\n\n");
            for (Event e : events) {
                String price = (e.getTicketPrice() == null || e.getTicketPrice().doubleValue() == 0)
                        ? "FREE" : "₹" + e.getTicketPrice();
                sb.append("🔹 ").append(e.getTitle()).append(": ").append(price).append("\n");
            }
            return sb.toString().trim();
        }

        // ── Seat availability ─────────────────────────────────────────────
        if (lower.contains("seat") || lower.contains("available") || lower.contains("capacity")
                || lower.contains("slot") || lower.contains("space")) {
            if (events.isEmpty()) return "No events found to show seat availability for.";
            StringBuilder sb = new StringBuilder("💺 Seat Availability:\n\n");
            for (Event e : events) {
                String seats = e.getAvailableSeats() != null
                        ? e.getAvailableSeats() + " seats available"
                        : "Check event page";
                sb.append("🔹 ").append(e.getTitle()).append(": ").append(seats).append("\n");
            }
            return sb.toString().trim();
        }

        // ── Department filter ─────────────────────────────────────────────
        if (lower.contains("department") || lower.contains("dept") || lower.contains("cs")
                || lower.contains("computer") || lower.contains("management")
                || lower.contains("electronics") || lower.contains("mechanical")) {
            if (events.isEmpty()) return "No events found.";
            StringBuilder sb = new StringBuilder("🏛️ Events by Department:\n\n");
            for (Event e : events) {
                sb.append("🔹 ").append(e.getTitle()).append(" → ").append(e.getDepartment()).append("\n");
            }
            return sb.toString().trim();
        }

        // ── QR Ticket ─────────────────────────────────────────────────────
        if (lower.contains("ticket") || lower.contains("qr") || lower.contains("download")
                || lower.contains("pdf") || lower.contains("pass")) {
            return "🎫 To get your QR ticket:\n\n"
                    + "1. Go to My Registrations page\n"
                    + "2. Enter your registered email address\n"
                    + "3. Click 'Download Ticket' on any registration\n"
                    + "4. A QR code PNG will download — show it at the event!\n\n"
                    + "The QR code contains your registration details.";
        }

        // ── About the app ─────────────────────────────────────────────────
        if (lower.contains("about") || lower.contains("purpose") || lower.contains("app")
                || lower.contains("application") || lower.contains("platform")
                || lower.contains("smartcampus") || lower.contains("use")) {
            return "🎓 SmartCampus is your college event management platform!\n\n"
                    + "✅ Browse upcoming events\n"
                    + "✅ Register with email OTP verification\n"
                    + "✅ View your registrations\n"
                    + "✅ Download QR Code tickets\n"
                    + "✅ Get email reminders 24 hours before events\n"
                    + "✅ View event locations on Google Maps\n\n"
                    + "Ask me anything about events!";
        }

        // ── Timing / Date ─────────────────────────────────────────────────
        if (lower.contains("time") || lower.contains("date") || lower.contains("when")
                || lower.contains("schedule") || lower.contains("timing")) {
            if (events.isEmpty()) return "No events scheduled right now.";
            StringBuilder sb = new StringBuilder("🕐 Event Schedule:\n\n");
            for (Event e : events) {
                sb.append("🔹 ").append(e.getTitle())
                  .append(": ").append(e.getEventDate() != null ? e.getEventDate().format(FMT) : "TBD")
                  .append("\n");
            }
            return sb.toString().trim();
        }

        // ── Default fallback ──────────────────────────────────────────────
        return "🤔 I can help you with:\n\n"
                + "• Type 'list events' → see all upcoming events\n"
                + "• Type 'how to register' → registration guide\n"
                + "• Type 'venues' → event locations\n"
                + "• Type 'free events' → pricing info\n"
                + "• Type 'available seats' → seat availability\n"
                + "• Type 'download ticket' → QR ticket guide\n"
                + "• Type 'about' → about SmartCampus";
    }
}
