package com.carpooling.carpooling.controllers.mvc;


import com.carpooling.carpooling.exceptions.AuthorizationException;
import com.carpooling.carpooling.helpers.AuthenticationHelper;
import com.carpooling.carpooling.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeMvcController {

    private final AuthenticationHelper authHelper;

    @Autowired
    public HomeMvcController(AuthenticationHelper authHelper) {
        this.authHelper = authHelper;
    }

    @GetMapping("/")
    public String showHomePage(HttpSession session, Model model) {
        User user = null;
        try {
            user = authHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            // It's okay if the user is not logged in for the home page
        }
        model.addAttribute("user", user);
        return "index";
    }

}
