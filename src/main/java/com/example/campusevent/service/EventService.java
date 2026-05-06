package com.example.campusevent.service;

import com.example.campusevent.entity.Event;
import com.example.campusevent.exception.EventNotFoundException;
import com.example.campusevent.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllUpcomingEvents() {
        return eventRepository.findByEventDateAfterOrderByEventDateAsc(LocalDateTime.now());
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAllByOrderByEventDateAsc();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));
    }

    public Event saveEvent(Event event) {
        if (event.getAvailableSeats() == null) {
            event.setAvailableSeats(event.getCapacity());
        }
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Event existing = getEventById(id);
        int seatsDiff = updatedEvent.getCapacity() - existing.getCapacity();
        existing.setTitle(updatedEvent.getTitle());
        existing.setDepartment(updatedEvent.getDepartment());
        existing.setType(updatedEvent.getType());
        existing.setDescription(updatedEvent.getDescription());
        existing.setEventDate(updatedEvent.getEventDate());
        existing.setVenue(updatedEvent.getVenue());
        existing.setCapacity(updatedEvent.getCapacity());
        existing.setAvailableSeats(Math.max(0, existing.getAvailableSeats() + seatsDiff));
        existing.setTicketPrice(updatedEvent.getTicketPrice());
        existing.setImageUrl(updatedEvent.getImageUrl());
        existing.setVenueAddress(updatedEvent.getVenueAddress());
        existing.setVenueLat(updatedEvent.getVenueLat());
        existing.setVenueLng(updatedEvent.getVenueLng());
        return eventRepository.save(existing);
    }

    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        eventRepository.delete(event);
    }

    public List<Event> filterEvents(String department, String type, String keyword) {
        LocalDateTime now = LocalDateTime.now();
        if (keyword != null && !keyword.isBlank()) {
            return eventRepository.searchByTitleAndUpcoming(keyword.trim(), now);
        }
        if (department != null && !department.isBlank() && type != null && !type.isBlank()) {
            return eventRepository.findByDepartmentIgnoreCaseAndTypeIgnoreCaseAndEventDateAfterOrderByEventDateAsc(department, type, now);
        }
        if (department != null && !department.isBlank()) {
            return eventRepository.findByDepartmentIgnoreCaseAndEventDateAfterOrderByEventDateAsc(department, now);
        }
        if (type != null && !type.isBlank()) {
            return eventRepository.findByTypeIgnoreCaseAndEventDateAfterOrderByEventDateAsc(type, now);
        }
        return getAllUpcomingEvents();
    }

    public void reduceSeats(Long eventId, int tickets) {
        Event event = getEventById(eventId);
        if (event.getAvailableSeats() < tickets) {
            throw new IllegalStateException("Not enough seats available. Only " + event.getAvailableSeats() + " seats left.");
        }
        event.setAvailableSeats(event.getAvailableSeats() - tickets);
        eventRepository.save(event);
    }

    public long countUpcomingEvents() {
        return eventRepository.countByEventDateAfter(LocalDateTime.now());
    }

    public boolean existsById(Long id) {
        return eventRepository.existsById(id);
    }

    /** Exposes the repository for REST controllers that need direct query access. */
    public com.example.campusevent.repository.EventRepository getEventRepository() {
        return eventRepository;
    }
}
