package com.example.campusevent.controller;

import com.example.campusevent.entity.Event;
import com.example.campusevent.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public String listEvents(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            Model model) {

        List<Event> events = eventService.filterEvents(department, type, keyword);
        model.addAttribute("events", events);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("selectedType", type);
        model.addAttribute("keyword", keyword);
        return "events";
    }

    @GetMapping("/{id}")
    public String eventDetail(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        return "event-detail";
    }
}
