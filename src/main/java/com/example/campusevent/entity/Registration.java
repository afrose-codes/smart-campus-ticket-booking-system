package com.example.campusevent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student name is required")
    @Size(max = 150)
    @Column(nullable = false)
    private String studentName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Department is required")
    @Column(nullable = false)
    private String department;

    @NotNull(message = "Tickets booked is required")
    @Min(value = 1, message = "At least 1 ticket must be booked")
    @Max(value = 10, message = "Maximum 10 tickets per registration")
    @Column(nullable = false)
    private Integer ticketsBooked;

    @Column(nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    public Registration() {}

    @PrePersist
    public void prePersist() {
        this.registeredAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getStudentName() { return studentName; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public Integer getTicketsBooked() { return ticketsBooked; }
    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public Event getEvent() { return event; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setEmail(String email) { this.email = email; }
    public void setDepartment(String department) { this.department = department; }
    public void setTicketsBooked(Integer ticketsBooked) { this.ticketsBooked = ticketsBooked; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }
    public void setEvent(Event event) { this.event = event; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Registration r = new Registration();
        public Builder studentName(String v) { r.studentName = v; return this; }
        public Builder email(String v) { r.email = v; return this; }
        public Builder department(String v) { r.department = v; return this; }
        public Builder ticketsBooked(Integer v) { r.ticketsBooked = v; return this; }
        public Builder event(Event v) { r.event = v; return this; }
        public Registration build() { return r; }
    }
}
