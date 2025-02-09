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
    private final TravelRepository travelRepository;
    private final UserService userService;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, TravelRepository travelRepository, UserService userService) {
        this.feedbackRepository = feedbackRepository;
        this.travelRepository = travelRepository;
        this.userService = userService;
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

    @Override
    public Page<FeedbackDto> getUserFeedbacks(long userId, int page, int size, String sortBy, String direction){
        User user = userService.getUserById(userId);

        Sort sort = Sort.by(Sort.Direction.fromString(direction),sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);

        Page<Feedback> feedbackPage = feedbackRepository.findByReceiver(user,pageable);

        return feedbackPage.map(feedback -> new FeedbackDto(feedback.getRating(), feedback.getComment(),feedback.getGiver().getUsername()));

    }
}
