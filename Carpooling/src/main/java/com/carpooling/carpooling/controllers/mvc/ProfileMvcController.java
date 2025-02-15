package com.carpooling.carpooling.controllers.mvc;

import com.carpooling.carpooling.exceptions.AuthorizationException;
import com.carpooling.carpooling.helpers.AuthenticationHelper;
import com.carpooling.carpooling.helpers.TravelMapper;
import com.carpooling.carpooling.helpers.UserMapper;
import com.carpooling.carpooling.models.Dtos.FeedbackDto;
import com.carpooling.carpooling.models.Dtos.TravelDto;
import com.carpooling.carpooling.models.Dtos.UserDto;
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
                return "redirect:/profile";
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
            return "redirect:/profile";
        } catch (AuthorizationException e) {
            model.addAttribute("error", "An error occurred while updating the profile.");
            return "ErrorView";
        }
    }
}