package com.carpooling.carpooling.controllers.mvc;

import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.services.interfaces.TravelService;
import com.carpooling.carpooling.helpers.AuthenticationHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/find/rides")
public class FindRidesMvcController {

    private final TravelService travelService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public FindRidesMvcController(TravelService travelService, AuthenticationHelper authenticationHelper) {
        this.travelService = travelService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public String showActiveRides(Model model, HttpSession session) {
        User currentUser = authenticationHelper.tryGetCurrentUser(session);

        List<Travel> userActiveTravels = travelService.getActiveTravelsByDriver(currentUser.getId());

        List<Travel> allActiveTravels = travelService.getAllActiveTravels();

        model.addAttribute("userActiveTravels", userActiveTravels);
        model.addAttribute("allActiveTravels", allActiveTravels);
        model.addAttribute("currentUser", currentUser);
        return "FindRidesView";
    }
}
