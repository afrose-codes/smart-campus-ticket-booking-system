package com.example.campusevent.controller.rest;

import com.example.campusevent.service.EventService;
import com.example.campusevent.service.FeedbackService;
import com.example.campusevent.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsRestController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(Map.of(
            "totalEvents", eventService.getAllEvents().size(),
            "upcomingEvents", eventService.countUpcomingEvents(),
            "totalRegistrations", registrationService.countAllRegistrations(),
            "totalTickets", registrationService.sumAllTickets(),
            "registrationStats", registrationService.getRegistrationStats(),
            "feedbackStats", feedbackService.getFeedbackStats()
        ));
    }
}
