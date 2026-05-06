package com.example.campusevent.repository;

import com.example.campusevent.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Upcoming events only
    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDateTime now);

    // Filter by department
    List<Event> findByDepartmentIgnoreCaseAndEventDateAfterOrderByEventDateAsc(String department, LocalDateTime now);

    // Filter by type
    List<Event> findByTypeIgnoreCaseAndEventDateAfterOrderByEventDateAsc(String type, LocalDateTime now);

    // Filter by department and type
    List<Event> findByDepartmentIgnoreCaseAndTypeIgnoreCaseAndEventDateAfterOrderByEventDateAsc(
            String department, String type, LocalDateTime now);

    // Search by title keyword
    @Query("SELECT e FROM Event e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) AND e.eventDate > :now ORDER BY e.eventDate ASC")
    List<Event> searchByTitleAndUpcoming(@Param("keyword") String keyword, @Param("now") LocalDateTime now);

    // Events on a specific date range
    @Query("SELECT e FROM Event e WHERE e.eventDate BETWEEN :start AND :end ORDER BY e.eventDate ASC")
    List<Event> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Count all upcoming
    long countByEventDateAfter(LocalDateTime now);

    // All events ordered by date
    List<Event> findAllByOrderByEventDateAsc();
}
