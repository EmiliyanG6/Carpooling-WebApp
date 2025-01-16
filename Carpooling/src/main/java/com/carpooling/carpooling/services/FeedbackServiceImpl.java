package com.carpooling.carpooling.services;

import com.carpooling.carpooling.models.Feedback;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.repositories.FeedbackRepository;
import com.carpooling.carpooling.repositories.TravelRepository;
import com.carpooling.carpooling.services.interfaces.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final TravelRepository travelRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, TravelRepository travelRepository) {
        this.feedbackRepository = feedbackRepository;
        this.travelRepository = travelRepository;
    }

    @Override
    public Feedback createFeedback(Long travelId, Feedback feedback) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(()-> new IllegalArgumentException("Travel not found"));

        feedback.setTravel(travel);

        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> getFeedback(Long travelId) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(()-> new IllegalArgumentException("Travel not found"));

        return feedbackRepository.findByTravelId(travel.getId());
    }

    @Override
    public void deleteFeedback(Long feedbackId) {
        if (!feedbackRepository.existsById(feedbackId)){
            throw new IllegalArgumentException("Feedback not found");
        }
        feedbackRepository.deleteById(feedbackId);
    }
}
