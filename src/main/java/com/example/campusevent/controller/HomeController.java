package com.example.campusevent.controller;

import com.example.campusevent.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private EventService eventService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("upcomingCount", eventService.countUpcomingEvents());
        model.addAttribute("featuredEvents",
                eventService.getAllUpcomingEvents().stream().limit(3).toList());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // /signup and /register (no event ID) both go to user account registration
    @GetMapping("/signup")
    public String signup() {
        return "redirect:/user-register";
    }

    // Plain /register with no ID goes to user account creation, not event booking
    @GetMapping("/register")
    public String register() {
        return "redirect:/user-register";
    }

    @GetMapping("/student-dashboard")
    public String studentDashboard(Model model) {
        model.addAttribute("upcomingEvents",
                eventService.getAllUpcomingEvents().stream().limit(6).toList());
        model.addAttribute("totalEvents", eventService.countUpcomingEvents());
        return "student-dashboard";
    }
}
