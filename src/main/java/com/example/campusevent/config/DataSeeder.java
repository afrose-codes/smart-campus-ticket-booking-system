package com.example.campusevent.config;

import com.example.campusevent.entity.Event;
import com.example.campusevent.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public void run(String... args) {
        if (eventRepository.count() == 0) {
            List<Event> events = List.of(
                Event.builder()
                    .title("National Tech Fest 2025")
                    .department("Computer Science")
                    .type("Technical Fest")
                    .description("A grand national-level technical festival featuring hackathons, coding contests, robotics, AI showcases, and project exhibitions. Open to all engineering students.")
                    .eventDate(LocalDateTime.now().plusDays(15))
                    .venue("Main Auditorium & Tech Block")
                    .capacity(500)
                    .availableSeats(500)
                    .ticketPrice(new BigDecimal("199.00"))
                    .imageUrl("https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=600")
                    .build(),

                Event.builder()
                    .title("AI & Machine Learning Workshop")
                    .department("Computer Science")
                    .type("Workshop")
                    .description("Hands-on workshop on Artificial Intelligence and Machine Learning fundamentals. Learn Python, TensorFlow, and build your first ML model with industry experts.")
                    .eventDate(LocalDateTime.now().plusDays(7))
                    .venue("CS Lab 301, Block B")
                    .capacity(60)
                    .availableSeats(60)
                    .ticketPrice(new BigDecimal("99.00"))
                    .imageUrl("https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=600")
                    .build(),

                Event.builder()
                    .title("Entrepreneurship & Startup Summit")
                    .department("Management")
                    .type("Seminar")
                    .description("Connect with successful entrepreneurs, VCs, and startup founders. Pitch your ideas, attend panel discussions, and network with the startup ecosystem.")
                    .eventDate(LocalDateTime.now().plusDays(20))
                    .venue("Seminar Hall, Admin Block")
                    .capacity(200)
                    .availableSeats(200)
                    .ticketPrice(BigDecimal.ZERO)
                    .imageUrl("https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=600")
                    .build(),

                Event.builder()
                    .title("Cultural Night - Rhythm & Beats")
                    .department("All Departments")
                    .type("Cultural Event")
                    .description("Annual cultural extravaganza featuring music, dance, drama, and art performances by students. A night to celebrate talent, creativity, and campus spirit.")
                    .eventDate(LocalDateTime.now().plusDays(30))
                    .venue("Open Air Amphitheatre")
                    .capacity(1000)
                    .availableSeats(1000)
                    .ticketPrice(new BigDecimal("49.00"))
                    .imageUrl("https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=600")
                    .build(),

                Event.builder()
                    .title("Web Development Bootcamp")
                    .department("Computer Science")
                    .type("Workshop")
                    .description("Intensive 2-day bootcamp covering HTML, CSS, JavaScript, React, and Spring Boot. Build a full-stack project from scratch with mentorship from senior developers.")
                    .eventDate(LocalDateTime.now().plusDays(10))
                    .venue("Innovation Lab, Block C")
                    .capacity(40)
                    .availableSeats(40)
                    .ticketPrice(new BigDecimal("149.00"))
                    .imageUrl("https://images.unsplash.com/photo-1461749280684-dccba630e2f6?w=600")
                    .build(),

                Event.builder()
                    .title("Research Paper Presentation")
                    .department("Electronics")
                    .type("Conference")
                    .description("Annual research symposium where students and faculty present their research papers on emerging technologies in electronics, IoT, and embedded systems.")
                    .eventDate(LocalDateTime.now().plusDays(25))
                    .venue("Conference Hall, Research Block")
                    .capacity(150)
                    .availableSeats(150)
                    .ticketPrice(BigDecimal.ZERO)
                    .imageUrl("https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?w=600")
                    .build()
            );
            eventRepository.saveAll(events);
            System.out.println("✅ Sample events seeded successfully!");
        }
    }
}
