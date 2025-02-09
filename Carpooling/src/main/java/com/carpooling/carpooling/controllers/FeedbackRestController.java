package com.carpooling.carpooling.controllers;


import com.carpooling.carpooling.models.Dtos.FeedbackDto;
import com.carpooling.carpooling.services.interfaces.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackRestController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackRestController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/user/{userId}")
    public Page<FeedbackDto> getUserFeedback(@PathVariable long userId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id") String sortBy,
                                             @RequestParam(defaultValue = "DESC") String direction){
        try {
            return feedbackService.getUserFeedbacks(userId,page,size,sortBy,direction);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch feedbacks");
        }
    }
}
