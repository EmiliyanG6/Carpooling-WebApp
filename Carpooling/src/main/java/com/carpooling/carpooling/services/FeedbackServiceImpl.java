package com.carpooling.carpooling.services;

import com.carpooling.carpooling.models.Dtos.FeedbackDto;
import com.carpooling.carpooling.models.Feedback;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.repositories.FeedbackRepository;
import com.carpooling.carpooling.repositories.TravelRepository;
import com.carpooling.carpooling.services.interfaces.FeedbackService;
import com.carpooling.carpooling.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserService userService;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, UserService userService) {
        this.feedbackRepository = feedbackRepository;
        this.userService = userService;
    }
    @Override
    public Feedback createFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }



    @Override
    public Page<FeedbackDto> getUserFeedbacks(long userId, int page, int size, String sortBy, String direction) {
        User user = userService.getUserById(userId);
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Feedback> feedbackPage = feedbackRepository.findByReceiver(user, pageable);

        // Convert Page<Feedback> to Page<FeedbackDto>
        return feedbackPage.map(feedback ->
                new FeedbackDto(feedback.getRating(), feedback.getComment(), feedback.getGiver().getUsername())
        );
    }


    @Override
    public void deleteFeedback(Long feedbackId, User currentUser) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found"));

        // Prevent deleting feedback if there's no valid user
        if (currentUser == null) {
            throw new SecurityException("You must be logged in to delete feedback.");
        }

        // Ensure only the giver or an admin can delete feedback
        if (feedback.getGiver().getId()!=(currentUser.getId()) && !currentUser.isAdmin()) {
            throw new SecurityException("You are not allowed to delete this feedback.");
        }

        feedbackRepository.delete(feedback);
    }

}

