package com.carpooling.carpooling.controllers.mvc;

import com.carpooling.carpooling.exceptions.AuthorizationException;
import com.carpooling.carpooling.helpers.AuthenticationHelper;
import com.carpooling.carpooling.helpers.TravelMapper;
import com.carpooling.carpooling.helpers.UserMapper;
import com.carpooling.carpooling.models.Dtos.FeedbackDto;
import com.carpooling.carpooling.models.Dtos.TravelDto;
import com.carpooling.carpooling.models.Dtos.UserDto;
import com.carpooling.carpooling.models.Feedback;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.services.interfaces.FeedbackService;
import com.carpooling.carpooling.services.interfaces.TravelService;
import com.carpooling.carpooling.services.interfaces.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    private final UserMapper userMapper;

    @Autowired
    public ProfileMvcController(UserService userService,
                                TravelService travelService,
                                AuthenticationHelper authenticationHelper,
                                TravelMapper travelMapper,
                                FeedbackService feedbackService,
                                UserMapper userMapper) {
        this.userService = userService;
        this.travelService = travelService;
        this.authenticationHelper = authenticationHelper;
        this.travelMapper = travelMapper;
        this.feedbackService = feedbackService;
        this.userMapper = userMapper;
    }


    @GetMapping("/{id}")
    public String showProfile(@PathVariable long id, Model model, HttpSession session) {
        User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
        User profileUser = userService.getUserById(id);

        boolean isOwner = loggedInUser != null && loggedInUser.getId() == profileUser.getId();

        List<TravelDto> activeTravels = travelMapper.toDtoList(travelService.getActiveTravelsByUser(profileUser.getId()));
        List<TravelDto> completedTravels = travelMapper.toDtoList(travelService.getCompletedTravelsByUser(profileUser.getId()));
        List<TravelDto> canceledTravels = travelMapper.toDtoList(travelService.getCanceledTravelsByUser(profileUser.getId()));

        Page<FeedbackDto> feedbackPage = feedbackService.getUserFeedbacks(profileUser.getId(), 0, 10, "id", "desc");
        List<FeedbackDto> feedbacks = feedbackPage.getContent()
                .stream()
                .map(feedback -> new FeedbackDto(feedback.getRating(), feedback.getComment(), feedback.getGiverUsername()))
                .toList();

        // Store travels in the model
        List<Travel> travels = travelService.getCompletedTravelsByUser(profileUser.getId());
        model.addAttribute("travels", travels);

        // Ensure at least one travel exists and pass the latest travel ID
        if (!travels.isEmpty()) {
            model.addAttribute("latestTravelId", travels.get(0).getId()); // Get the first travel ID
        }

        model.addAttribute("user", profileUser);
        model.addAttribute("activeTravels", activeTravels);
        model.addAttribute("completedTravels", completedTravels);
        model.addAttribute("canceledTravels", canceledTravels);
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("isOwner", isOwner);

        return "ProfileView";
    }

    @GetMapping("/{id}/edit")
    public String showEditProfileForm(@PathVariable long id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (Exception e) {
            return "redirect:/auth/login";
        }
        User user = userService.getUserById(id);
        UserDto userDto = userMapper.toDto(user);
        model.addAttribute("userId", id);
        model.addAttribute("user", userDto);
        return "ProfileEditView";
    }

    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable long id,
                             @Valid @ModelAttribute("user") UserDto userDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        User currentUser;

        try {
            currentUser = authenticationHelper.tryGetCurrentUser(session);
            if (currentUser.getId() != id) {
                return "redirect:/profile/" + id;
            }
        } catch (Exception e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "ProfileEditView";
        }

        try {
            User userToUpdate = userMapper.toEntity(userDto);
            userService.update(id, userToUpdate);
            model.addAttribute("currentUser",currentUser);
            model.addAttribute("success", "Changed user successfully");
            return "redirect:/profile/"+ userToUpdate.getId();
        } catch (AuthorizationException e) {
            model.addAttribute("error", "An error occurred while updating the profile.");
            return "ErrorView";
        }
    }
}