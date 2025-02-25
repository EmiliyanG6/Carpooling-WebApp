package com.carpooling.carpooling.controllers.mvc;

import com.carpooling.carpooling.helpers.AuthenticationHelper;
import com.carpooling.carpooling.models.Dtos.FeedbackDto;
import com.carpooling.carpooling.models.Feedback;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.services.interfaces.FeedbackService;
import com.carpooling.carpooling.services.interfaces.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/feedback")
public class FeedbackMvcController {

    private final FeedbackService feedbackService;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;


    @Autowired
    public FeedbackMvcController(FeedbackService feedbackService, AuthenticationHelper authenticationHelper,
                                 UserService userService) {
        this.feedbackService = feedbackService;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;

    }

    // Show feedback for a specific user
    @GetMapping("/{userId}")
    public String viewUserFeedback(@PathVariable Long userId, Model model) {
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String direction = "ASC";

        Page<FeedbackDto> feedbacks = feedbackService.getUserFeedbacks(userId, page, size, sortBy, direction);

        model.addAttribute("feedbacks", feedbacks.getContent());
        return "feedback-list";
    }

    // Post feedback for a user
    @PostMapping("/{receiverId}")
    public String leaveFeedback(@PathVariable Long receiverId,
                                @RequestParam String comment,
                                @RequestParam int rating,
                                HttpSession session) {
        User giver = authenticationHelper.tryGetCurrentUser(session);
        User receiver = userService.getUserById(receiverId);

        Feedback feedback = new Feedback();
        feedback.setGiver(giver);
        feedback.setReceiver(receiver);
        feedback.setComment(comment);
        feedback.setRating(rating);

        feedbackService.createFeedback(feedback);

        return "redirect:/profile/" + receiverId;
    }




    // Delete feedback
    @PostMapping("/delete/{feedbackId}")
    public String deleteFeedback(@PathVariable Long feedbackId, HttpSession session) {
        User currentUser = authenticationHelper.tryGetCurrentUser(session);

        try {
            feedbackService.deleteFeedback(feedbackId, currentUser);
        } catch (Exception e) {
            return "redirect:/error";
        }

        return "redirect:/profile/" + currentUser.getId();
    }
}
