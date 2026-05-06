package com.example.campusevent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Department is required")
    @Column(nullable = false)
    private String department;

    @NotBlank(message = "Event type is required")
    @Column(nullable = false)
    private String type;

    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Event date is required")
    @Column(nullable = false)
    private LocalDateTime eventDate;

    @NotBlank(message = "Venue is required")
    @Column(nullable = false)
    private String venue;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer availableSeats;

    @Column(precision = 10, scale = 2)
    private BigDecimal ticketPrice;

    @Column
    private String imageUrl;

    // Feature 3: Google Maps venue fields (nullable — existing records unaffected)
    @Column(name = "venue_address")
    private String venueAddress;

    @Column(name = "venue_lat")
    private Double venueLat;

    @Column(name = "venue_lng")
    private Double venueLng;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Registration> registrations;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Feedback> feedbacks;

    public Event() {}

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.availableSeats == null) {
            this.availableSeats = this.capacity;
        }
    }

    public boolean isUpcoming() {
        return this.eventDate != null && this.eventDate.isAfter(LocalDateTime.now());
    }

    public boolean isFull() {
        return this.availableSeats != null && this.availableSeats <= 0;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDepartment() { return department; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public LocalDateTime getEventDate() { return eventDate; }
    public String getVenue() { return venue; }
    public Integer getCapacity() { return capacity; }
    public Integer getAvailableSeats() { return availableSeats; }
    public BigDecimal getTicketPrice() { return ticketPrice; }
    public String getImageUrl() { return imageUrl; }
    public String getVenueAddress() { return venueAddress; }
    public Double getVenueLat() { return venueLat; }
    public Double getVenueLng() { return venueLng; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<Registration> getRegistrations() { return registrations; }
    public List<Feedback> getFeedbacks() { return feedbacks; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDepartment(String department) { this.department = department; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }
    public void setVenue(String venue) { this.venue = venue; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setVenueAddress(String venueAddress) { this.venueAddress = venueAddress; }
    public void setVenueLat(Double venueLat) { this.venueLat = venueLat; }
    public void setVenueLng(Double venueLng) { this.venueLng = venueLng; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setRegistrations(List<Registration> registrations) { this.registrations = registrations; }
    public void setFeedbacks(List<Feedback> feedbacks) { this.feedbacks = feedbacks; }

    // Builder pattern (manual)
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Event event = new Event();
        public Builder title(String v) { event.title = v; return this; }
        public Builder department(String v) { event.department = v; return this; }
        public Builder type(String v) { event.type = v; return this; }
        public Builder description(String v) { event.description = v; return this; }
        public Builder eventDate(LocalDateTime v) { event.eventDate = v; return this; }
        public Builder venue(String v) { event.venue = v; return this; }
        public Builder capacity(Integer v) { event.capacity = v; return this; }
        public Builder availableSeats(Integer v) { event.availableSeats = v; return this; }
        public Builder ticketPrice(BigDecimal v) { event.ticketPrice = v; return this; }
        public Builder imageUrl(String v) { event.imageUrl = v; return this; }
        public Builder venueAddress(String v) { event.venueAddress = v; return this; }
        public Builder venueLat(Double v) { event.venueLat = v; return this; }
        public Builder venueLng(Double v) { event.venueLng = v; return this; }
        public Event build() { return event; }
    }
}
