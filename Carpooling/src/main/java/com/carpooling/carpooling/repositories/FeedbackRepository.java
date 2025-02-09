package com.carpooling.carpooling.repositories;

import com.carpooling.carpooling.models.Feedback;
import com.carpooling.carpooling.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByTravelId(Long travelId);
    List<Feedback> findByGiverId(Long giverId);
    Page<Feedback> findByReceiver(User receiver, Pageable pageable);
}
