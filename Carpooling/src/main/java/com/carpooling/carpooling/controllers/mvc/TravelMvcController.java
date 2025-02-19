package com.carpooling.carpooling.controllers.mvc;

import com.carpooling.carpooling.enums.PassengerStatus;
import com.carpooling.carpooling.exceptions.AuthorizationException;
import com.carpooling.carpooling.helpers.AuthenticationHelper;
import com.carpooling.carpooling.helpers.TravelMapper;
import com.carpooling.carpooling.models.Dtos.TravelDto;
import com.carpooling.carpooling.models.Passenger;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.services.interfaces.PassengerService;
import com.carpooling.carpooling.services.interfaces.TravelService;
import com.carpooling.carpooling.services.interfaces.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/travel")
public class TravelMvcController {

    private final TravelService travelService;
    private final AuthenticationHelper authenticationHelper;
    private final PassengerService passengerService;
    private final TravelMapper travelMapper;
    private final UserService userService;

    @Autowired
    public TravelMvcController(TravelService travelService, AuthenticationHelper authenticationHelper,
                               PassengerService passengerService, TravelMapper travelMapper,
                               UserService userService) {
        this.travelService = travelService;
        this.authenticationHelper = authenticationHelper;
        this.passengerService = passengerService;
        this.travelMapper = travelMapper;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String showCreateTravelForm(Model model, HttpSession session) {
        User currentUser = authenticationHelper.tryGetCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("travel",new Travel());
        model.addAttribute("currentUser", currentUser);
        return "CreateTravelView";
    }

    // Handle Travel Creation
    @PostMapping("/create")
    public String createTravel(@Valid @ModelAttribute("travel") Travel travel,
                               BindingResult bindingResult,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "CreateTravelView";
        }

        User currentUser = authenticationHelper.tryGetCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        travel.setDriver(currentUser);

        try {
            travelService.createTravel(currentUser,travel);
            redirectAttributes.addFlashAttribute("success", "Travel created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create travel: " + e.getMessage());
        }

        return "redirect:/profile/" + currentUser.getId();
    }



    @GetMapping("/{travelId}")
    public String showSingleTravel(@PathVariable long travelId, Model model, HttpSession session) {
        try {
            Travel travel = travelService.getTravelById(travelId);
            User currentUser = authenticationHelper.tryGetCurrentUser(session);

            boolean hasFreeSpots = travel.getFreeSpots() > 0;
            boolean isPassenger = travel.getPassengers().stream()
                    .anyMatch(passenger -> passenger.getUser().getId() == (currentUser.getId()));

            List<Passenger> allPassengers = passengerService.getPassengersByTravel(travelId);
            List<Passenger> approvedPassengers = allPassengers.stream()
                    .filter(passenger -> PassengerStatus.APPROVED.equals(passenger.getStatus()))
                    .toList();

            boolean isCreator = currentUser.getId() == travel.getDriver().getId();

            model.addAttribute("isCreator", isCreator);
            model.addAttribute("travel", travel);
            model.addAttribute("isDriver", currentUser.getId() == (travel.getDriver().getId()));
            model.addAttribute("hasFreeSPorts", hasFreeSpots);
            model.addAttribute("isPassenger", isPassenger);
            model.addAttribute("passengers", allPassengers);
            model.addAttribute("approvedPassengers", approvedPassengers);
            model.addAttribute("currentUser", currentUser);

        } catch (Exception e) {
            model.addAttribute("error", "Could not load travel details.");
        }
        return "TravelDetailsView";
    }


    @PostMapping("/{travelId}/passengers/{passengerId}/remove")
    public String removePassenger(@PathVariable Long travelId, @PathVariable Long passengerId, HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            Travel travel = travelService.getTravelById(travelId);
            if (travel.getDriver().getId() != currentUser.getId()) {
                throw new AuthorizationException("Only the driver can remove passengers.");
            }
            passengerService.removePassangerFromTravel(travelId, passengerId);
            return "redirect:/travels/" + travelId;
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    @PostMapping("/{travelId}/complete")
    public String completeTravel(@PathVariable Long travelId, HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            Travel travel = travelService.getTravelById(travelId);
            if (travel.getDriver().getId() != currentUser.getId()) {
                throw new AuthorizationException("Only the driver can complete the travel.");
            }
            travelService.completeTravel(travelId,currentUser);
            return "redirect:/profile/"+ currentUser.getId();
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    @PostMapping("/{travelId}/cancel")
    public String cancelTravel(@PathVariable Long travelId, HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            Travel travel = travelService.getTravelById(travelId);
            if (travel.getDriver().getId() != currentUser.getId()) {
                throw new AuthorizationException("Only the driver can cancel the travel.");
            }
            travelService.canceTravel(travelId, currentUser);
            return "redirect:/profile/" + currentUser.getId();
        } catch (Exception e) {
            return "redirect:/error";
        }
    }
    @PostMapping("/{travelId}/apply")
    public String applyForTravel(@PathVariable Long travelId,
                                 HttpSession session,
                                 Model model) {
        User currentUser = null;
        try {
            currentUser = authenticationHelper.tryGetCurrentUser(session);
            Passenger passenger = new Passenger();
            passenger.setUser(currentUser);

            passengerService.addPassangerToTravel(travelId, passenger);
            model.addAttribute("success", "Successfully applied for the travel.");
        } catch (Exception e) {
            model.addAttribute("error", "Could not apply for the travel: " + e.getMessage());
        }

        return "redirect:/profile/" + currentUser.getId();
    }
    @PostMapping("/{travelId}/passengers/{passengerId}/approve")
    public String approvePassenger(@PathVariable Long travelId,
                                   @PathVariable Long passengerId,
                                   HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            System.out.println("DEBUG: Approve request received for travel ID: " + travelId + " and passenger ID: " + passengerId);
            System.out.println("DEBUG: Approving user: " + user.getUsername());

            travelService.approvePassenger(travelId, passengerId, user);

            System.out.println("DEBUG: Passenger approved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
        return "redirect:/travel/" + travelId;
    }

    @PostMapping("/{travelId}/passengers/{passengerId}/reject")
    public String rejectPassenger(@PathVariable Long travelId,
                                  @PathVariable Long passengerId,
                                  HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            travelService.rejectPassenger(travelId, passengerId, user);
        } catch (Exception e) {
            return "redirect:/travel/"+travelId;
        }
        return "redirect:/travel/" + travelId;
    }

    @GetMapping("/{travelId}/edit")
    public String editTravel(@PathVariable long travelId, Model model, HttpSession session) {
        try {
            Travel travel = travelService.getTravelById(travelId);
            TravelDto travelDto = travelMapper.toDto(travel);
            User currentUser = authenticationHelper.tryGetCurrentUser(session);

            if (travel.getDriver().getId() != currentUser.getId()) {
                throw new AuthorizationException("You are not authorized to edit this travel.");
            }

            model.addAttribute("travel", travelDto);
            model.addAttribute("currentUser", currentUser);
            return "EditTravelView";

        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/travel/" + travelId;
        } catch (Exception e) {
            model.addAttribute("error", "Error loading travel for editing.");
            return "redirect:/travel/" + travelId;
        }
    }

    @PostMapping("/{id}/edit")
    public String updateTravel(@PathVariable Long id,
                               @ModelAttribute TravelDto travelDto,
                               HttpSession session,
                               Model model) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);

            Travel travel = travelMapper.fromDto(id, travelDto);
            travelService.updateTravel(id, travel);

            model.addAttribute("success", "Travel updated successfully.");
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("travel", travelDto);

            return "EditTravelView";

        } catch (Exception e) {
            model.addAttribute("error", "Error updating travel. Please try again.");
            return "redirect:/travel/" + id;
        }
    }


}
