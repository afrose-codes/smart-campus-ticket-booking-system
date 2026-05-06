package com.example.campusevent.controller;

import com.example.campusevent.entity.Event;
import com.example.campusevent.service.EventService;
import com.example.campusevent.service.FeedbackService;
import com.example.campusevent.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalEvents", eventService.getAllEvents().size());
        model.addAttribute("upcomingEvents", eventService.countUpcomingEvents());
        model.addAttribute("totalRegistrations", registrationService.countAllRegistrations());
        model.addAttribute("totalTickets", registrationService.sumAllTickets());
        model.addAttribute("recentEvents", eventService.getAllEvents().stream().limit(5).toList());
        return "admin-dashboard";
    }

    @GetMapping("/events")
    public String adminEvents(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            Model model) {
        List<Event> events = eventService.filterEvents(department, type, keyword);
        model.addAttribute("events", events);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("selectedType", type);
        model.addAttribute("keyword", keyword);
        return "admin-events";
    }

    @GetMapping("/events/new")
    public String newEventForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("isEdit", false);
        return "admin-event-form";
    }

    @PostMapping("/events/new")
    public String createEvent(
            @Valid @ModelAttribute("event") Event event,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "admin-event-form";
        }
        eventService.saveEvent(event);
        redirectAttributes.addFlashAttribute("successMessage", "Event created successfully!");
        return "redirect:/admin/events";
    }

    @GetMapping("/events/edit/{id}")
    public String editEventForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        model.addAttribute("isEdit", true);
        return "admin-event-form";
    }

    @PostMapping("/events/edit/{id}")
    public String updateEvent(
            @PathVariable Long id,
            @Valid @ModelAttribute("event") Event event,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "admin-event-form";
        }
        eventService.updateEvent(id, event);
        redirectAttributes.addFlashAttribute("successMessage", "Event updated successfully!");
        return "redirect:/admin/events";
    }

    @PostMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        eventService.deleteEvent(id);
        redirectAttributes.addFlashAttribute("successMessage", "Event deleted successfully!");
        return "redirect:/admin/events";
    }

    @GetMapping("/stats")
    public String stats(Model model) {
        model.addAttribute("registrationStats", registrationService.getRegistrationStats());
        model.addAttribute("feedbackStats", feedbackService.getFeedbackStats());
        model.addAttribute("totalRegistrations", registrationService.countAllRegistrations());
        model.addAttribute("totalTickets", registrationService.sumAllTickets());
        model.addAttribute("totalEvents", eventService.getAllEvents().size());
        model.addAttribute("upcomingEvents", eventService.countUpcomingEvents());
        return "stats";
    }

    @GetMapping("/registrations/{eventId}")
    public String viewRegistrations(@PathVariable Long eventId, Model model) {
        model.addAttribute("event", eventService.getEventById(eventId));
        model.addAttribute("registrations", registrationService.getRegistrationsByEvent(eventId));
        return "admin-registrations";
    }
}
