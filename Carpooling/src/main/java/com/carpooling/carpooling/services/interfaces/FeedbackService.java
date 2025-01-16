package com.carpooling.carpooling.services.interfaces;

import com.carpooling.carpooling.models.Feedback;

import java.util.List;

public interface FeedbackService {
    Feedback createFeedback(Long travelId, Feedback feedback);
    List<Feedback> getFeedback(Long travelId);
    void deleteFeedback(Long feedbackId);
}
