package com.carpooling.carpooling.services.interfaces;

import com.carpooling.carpooling.models.Dtos.FeedbackDto;
import com.carpooling.carpooling.models.Feedback;
import com.carpooling.carpooling.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FeedbackService {
    Feedback createFeedback(Feedback feedback);
    Page<FeedbackDto> getUserFeedbacks(long userId, int page, int size, String sortBy, String direction);
    void deleteFeedback(Long feedbackId, User currentUser);


}
