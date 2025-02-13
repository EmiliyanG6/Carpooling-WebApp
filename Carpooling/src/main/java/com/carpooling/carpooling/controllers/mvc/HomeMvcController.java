package com.carpooling.carpooling.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeMvcController {

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

}
