package com.carpooling.carpooling.controllers.mvc;

import com.carpooling.carpooling.helpers.AuthenticationHelper;
import com.carpooling.carpooling.helpers.TravelMapper;
import com.carpooling.carpooling.models.Dtos.FeedbackDto;
import com.carpooling.carpooling.models.Dtos.TravelDto;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.services.interfaces.FeedbackService;
import com.carpooling.carpooling.services.interfaces.TravelService;
import com.carpooling.carpooling.services.interfaces.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.http.HttpHeaders;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/profile")
public class ProfileMvcController {

    private final UserService userService;
    private final TravelService travelService;
    private final TravelMapper travelMapper;
    private final AuthenticationHelper authenticationHelper;
    private final FeedbackService feedbackService;

    @Autowired
    public ProfileMvcController(UserService userService,
                                TravelService travelService,
                                AuthenticationHelper authenticationHelper,
                                TravelMapper travelMapper,
                                FeedbackService feedbackService) {
        this.userService = userService;
        this.travelService = travelService;
        this.authenticationHelper = authenticationHelper;
        this.travelMapper = travelMapper;
        this.feedbackService = feedbackService;
    }


    @GetMapping
    public String showProfile(Model model,HttpSession session) {

        User user = authenticationHelper.tryGetCurrentUser(session);

        List<TravelDto> activeTravels = travelMapper.toDtoList(travelService.getActiveTravels());
        List<TravelDto> completedTravels = travelMapper.toDtoList(travelService.getCompletedTravels());
        List<TravelDto> canceledTravels = travelMapper.toDtoList(travelService.getCanceledTravels());
        List<FeedbackDto> feedbacks = feedbackService.getUserFeedbacks(user.getId(), 0, 10, "id", "desc").getContent();

        model.addAttribute("user", user);
        model.addAttribute("activeTravels", activeTravels);
        model.addAttribute("completedTravels", completedTravels);
        model.addAttribute("canceledTravels", canceledTravels);
        model.addAttribute("feedbacks", feedbacks);

        return "ProfileView";

    }
}