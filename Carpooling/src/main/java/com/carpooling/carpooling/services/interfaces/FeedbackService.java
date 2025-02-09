package com.carpooling.carpooling.services.interfaces;

import com.carpooling.carpooling.models.Dtos.FeedbackDto;
import com.carpooling.carpooling.models.Feedback;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FeedbackService {
    Feedback createFeedback(Long travelId, Feedback feedback);
    List<Feedback> getFeedback(Long travelId);
    void deleteFeedback(Long feedbackId);

    Page<FeedbackDto> getUserFeedbacks(long userId, int page, int size, String sortBy, String direction);
}
