package com.example.campusevent.controller.rest;

import com.example.campusevent.entity.Event;
import com.example.campusevent.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventRestController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllUpcomingEvents() {
        return ResponseEntity.ok(eventService.getAllUpcomingEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Event>> filterEvents(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(eventService.filterEvents(department, type, keyword));
    }

    /**
     * Feature 6: Smart search endpoint — returns full event list matching filters as JSON.
     * GET /api/events/search?q=&dept=&type=&date=
     */
    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) String type) {
        return ResponseEntity.ok(eventService.filterEvents(dept, type, q));
    }

    /**
     * Feature 6: Autocomplete — returns top 5 event name suggestions matching the query.
     * GET /api/events/autocomplete?q={query}
     */
    @GetMapping("/autocomplete")
    public ResponseEntity<List<Map<String, Object>>> autocomplete(
            @RequestParam(defaultValue = "") String q) {
        if (q.isBlank()) {
            return ResponseEntity.ok(List.of());
        }
        List<Event> matches = eventService.getEventRepository()
                .searchByTitleAndUpcoming(q.trim(), LocalDateTime.now());
        List<Map<String, Object>> suggestions = matches.stream()
                .limit(5)
                .map(e -> Map.<String, Object>of(
                        "id", e.getId(),
                        "title", e.getTitle(),
                        "type", e.getType(),
                        "department", e.getDepartment()
                ))
                .toList();
        return ResponseEntity.ok(suggestions);
    }
}
