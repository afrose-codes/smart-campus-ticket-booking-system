package com.example.campusevent.repository;

import com.example.campusevent.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    // Find registrations by email
    List<Registration> findByEmailIgnoreCaseOrderByRegisteredAtDesc(String email);

    // Find registrations by event
    List<Registration> findByEventIdOrderByRegisteredAtDesc(Long eventId);

    // Count registrations per event
    long countByEventId(Long eventId);

    // Sum tickets booked per event
    @Query("SELECT COALESCE(SUM(r.ticketsBooked), 0) FROM Registration r WHERE r.event.id = :eventId")
    Integer sumTicketsByEventId(@Param("eventId") Long eventId);

    // Check if email already registered for event
    boolean existsByEmailIgnoreCaseAndEventId(String email, Long eventId);

    // Stats: registrations per event
    @Query("SELECT r.event.title, COUNT(r), SUM(r.ticketsBooked) FROM Registration r GROUP BY r.event.id, r.event.title ORDER BY COUNT(r) DESC")
    List<Object[]> getRegistrationStats();

    // Total registrations count
    @Query("SELECT COUNT(r) FROM Registration r")
    long countAllRegistrations();

    // Total tickets booked
    @Query("SELECT COALESCE(SUM(r.ticketsBooked), 0) FROM Registration r")
    long sumAllTickets();
}
