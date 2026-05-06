package com.example.campusevent.repository;

import com.example.campusevent.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByEventIdOrderBySubmittedAtDesc(Long eventId);

    long countByEventId(Long eventId);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.event.id = :eventId")
    Double avgRatingByEventId(@Param("eventId") Long eventId);

    @Query("SELECT f.event.title, COUNT(f), AVG(f.rating) FROM Feedback f GROUP BY f.event.id, f.event.title")
    List<Object[]> getFeedbackStats();
}
