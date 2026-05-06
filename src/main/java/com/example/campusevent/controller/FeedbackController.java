package com.example.campusevent.controller;

import com.example.campusevent.entity.Event;
import com.example.campusevent.entity.Feedback;
import com.example.campusevent.service.EventService;
import com.example.campusevent.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private EventService eventService;

    @GetMapping("/{eventId}")
    public String showFeedbackForm(@PathVariable Long eventId, Model model) {
        Event event = eventService.getEventById(eventId);
        model.addAttribute("event", event);
        model.addAttribute("feedback", new Feedback());
        return "feedback-form";
    }

    @PostMapping("/{eventId}")
    public String submitFeedback(
            @PathVariable Long eventId,
            @Valid @ModelAttribute("feedback") Feedback feedback,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        Event event = eventService.getEventById(eventId);

        if (result.hasErrors()) {
            model.addAttribute("event", event);
            return "feedback-form";
        }

        feedbackService.submitFeedback(feedback, eventId);
        redirectAttributes.addFlashAttribute("eventTitle", event.getTitle());
        return "redirect:/feedback/success";
    }

    @GetMapping("/success")
    public String feedbackSuccess() {
        return "feedback-success";
    }
}
