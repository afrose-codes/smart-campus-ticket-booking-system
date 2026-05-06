package com.example.campusevent.controller;

import com.example.campusevent.entity.Event;
import com.example.campusevent.entity.Registration;
import com.example.campusevent.service.EventService;
import com.example.campusevent.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private EventService eventService;

    private static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
    private static final DateTimeFormatter D_FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    @GetMapping("/register/{eventId}")
    public String showRegistrationForm(@PathVariable Long eventId, Model model) {
        Event event = eventService.getEventById(eventId);
        model.addAttribute("event", event);
        model.addAttribute("registration", new Registration());
        return "register";
    }

    @PostMapping("/register/{eventId}")
    public String submitRegistration(
            @PathVariable Long eventId,
            @Valid @ModelAttribute("registration") Registration registration,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        Event event = eventService.getEventById(eventId);

        if (result.hasErrors()) {
            model.addAttribute("event", event);
            return "register";
        }

        try {
            Registration saved = registrationService.register(registration, eventId);
            redirectAttributes.addFlashAttribute("regStudentName", saved.getStudentName());
            redirectAttributes.addFlashAttribute("regEmail", saved.getEmail());
            redirectAttributes.addFlashAttribute("regTickets", saved.getTicketsBooked());
            redirectAttributes.addFlashAttribute("regTime",
                    saved.getRegisteredAt() != null
                            ? saved.getRegisteredAt().format(DT_FMT) : "");
            redirectAttributes.addFlashAttribute("eventTitle", event.getTitle());
            redirectAttributes.addFlashAttribute("regId", saved.getId());
            return "redirect:/registration-success";
        } catch (Exception ex) {
            model.addAttribute("event", event);
            model.addAttribute("errorMessage", ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/registration-success")
    public String registrationSuccess() {
        return "registration-success";
    }

    @GetMapping("/my-registrations")
    public String myRegistrations(
            @RequestParam(required = false) String email,
            Model model) {

        // Always add email to model (may be null — template handles it)
        model.addAttribute("email", email != null ? email.trim() : "");

        if (email == null || email.isBlank()) {
            // No email provided — show empty search form
            model.addAttribute("registrations", Collections.emptyList());
            model.addAttribute("searched", false);
            return "my-registrations";
        }

        try {
            List<Registration> registrations =
                    registrationService.getRegistrationsByEmail(email.trim());

            // Pre-format dates in Java to avoid #temporals dependency in template
            for (Registration reg : registrations) {
                if (reg.getEvent() != null && reg.getEvent().getEventDate() != null) {
                    // Store formatted strings as transient — we'll use model maps instead
                }
            }

            model.addAttribute("registrations", registrations);
            model.addAttribute("searched", true);

            if (registrations.isEmpty()) {
                model.addAttribute("noResults", true);
            }

        } catch (Exception e) {
            System.err.println("Error fetching registrations for email: " + email);
            e.printStackTrace();
            model.addAttribute("registrations", Collections.emptyList());
            model.addAttribute("searched", true);
            model.addAttribute("fetchError", "Could not load registrations. Please try again.");
        }

        return "my-registrations";
    }
}
