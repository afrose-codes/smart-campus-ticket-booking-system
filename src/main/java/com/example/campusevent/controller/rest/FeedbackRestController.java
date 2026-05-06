package com.example.campusevent.controller.rest;

import com.example.campusevent.entity.Feedback;
import com.example.campusevent.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackRestController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/{eventId}")
    public ResponseEntity<?> submitFeedback(
            @PathVariable Long eventId,
            @Valid @RequestBody Feedback feedback) {
        try {
            Feedback saved = feedbackService.submitFeedback(feedback, eventId);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getFeedbackByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(Map.of(
            "feedbacks", feedbackService.getFeedbackByEvent(eventId),
            "averageRating", feedbackService.getAvgRating(eventId),
            "count", feedbackService.countByEvent(eventId)
        ));
    }
}
