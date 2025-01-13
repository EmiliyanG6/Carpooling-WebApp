package com.carpooling.carpooling.repositories;

import com.carpooling.carpooling.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByTravelId(Long travelId);
    List<Feedback> findByGiverId(Long giverId);
    List<Feedback> findByReceiverId(Long receiverId);
}
