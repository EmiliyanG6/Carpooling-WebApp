package com.carpooling.carpooling.controllers.mvc;


import com.carpooling.carpooling.models.Dtos.LoginDto;
import com.carpooling.carpooling.models.Dtos.RegisterDto;
import com.carpooling.carpooling.exceptions.AuthorizationException;
import com.carpooling.carpooling.exceptions.EntityDuplicateException;
import com.carpooling.carpooling.helpers.AuthenticationHelper;
import com.carpooling.carpooling.helpers.UserMapper;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.services.interfaces.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    @Autowired
    public AuthenticationMvcController(UserService userService,
                                       AuthenticationHelper authenticationHelper,
                                       UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model){
        model.addAttribute("login",new LoginDto());
        return "LoginView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto login,
                              BindingResult bindingResult,
                              HttpSession session){
        if (bindingResult.hasErrors()) {
            return "LoginView";
        }

        try {
            authenticationHelper.verifyAutnentication(login.getUsername(), login.getPassword());
            session.setAttribute("currentUser",login.getUsername());
            return "redirect:/";
        }catch (AuthorizationException e){
            bindingResult.rejectValue("username","auth_error",e.getMessage());
            return "LoginView";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session){
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model){
        model.addAttribute("register",new RegisterDto());
        return "RegisterView";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto register,
                                 BindingResult bindingResult){
        System.out.println("Registration request received: " + register.getUsername());
        if (bindingResult.hasErrors()) {
            System.out.println("Validation errors: " + bindingResult.getAllErrors());
            return "RegisterView";
        }

        if (!register.getPassword().equals(register.getPasswordConfirm())){
            bindingResult.rejectValue("passwordConfirm","password_error",
                    "Password confirmation should match password.");
            return "RegisterView";
        }

        try {
            User user = userMapper.fromDto(register);
            userService.create(user);
            System.out.println("User created successfully!");
            return "redirect:/auth/login";
        }catch (EntityDuplicateException e){
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            System.out.println("Error: " + e.getMessage());
            return "RegisterView";
        }
    }
    @GetMapping("/error")
    public String showErrorPage(Model model, HttpSession session) {
        String username = (String) session.getAttribute("currentUser");
        if (username != null) {
            User user = userService.get(username);  // Get user from DB
            if (user != null) {
                model.addAttribute("userId", user.getId());
            }
        }
        model.addAttribute("error", "An unexpected error occurred.");
        return "ErrorView";
    }
}
