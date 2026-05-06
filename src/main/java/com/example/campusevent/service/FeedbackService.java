package com.example.campusevent.service;

import com.example.campusevent.entity.Event;
import com.example.campusevent.entity.Feedback;
import com.example.campusevent.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private EventService eventService;

    public Feedback submitFeedback(Feedback feedback, Long eventId) {
        Event event = eventService.getEventById(eventId);
        feedback.setEvent(event);
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbackByEvent(Long eventId) {
        return feedbackRepository.findByEventIdOrderBySubmittedAtDesc(eventId);
    }

    public Double getAvgRating(Long eventId) {
        return feedbackRepository.avgRatingByEventId(eventId);
    }

    public List<Object[]> getFeedbackStats() {
        return feedbackRepository.getFeedbackStats();
    }

    public long countByEvent(Long eventId) {
        return feedbackRepository.countByEventId(eventId);
    }
}
