package com.example.campusevent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student name is required")
    @Size(max = 150)
    @Column(nullable = false)
    private String studentName;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    @Column(nullable = false)
    private Integer rating;

    @NotBlank(message = "Comment is required")
    @Size(min = 10, max = 1000, message = "Comment must be between 10 and 1000 characters")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    public Feedback() {}

    @PrePersist
    public void prePersist() {
        this.submittedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getStudentName() { return studentName; }
    public Integer getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public Event getEvent() { return event; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public void setEvent(Event event) { this.event = event; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Feedback f = new Feedback();
        public Builder studentName(String v) { f.studentName = v; return this; }
        public Builder rating(Integer v) { f.rating = v; return this; }
        public Builder comment(String v) { f.comment = v; return this; }
        public Builder event(Event v) { f.event = v; return this; }
        public Feedback build() { return f; }
    }
}
