package com.carpooling.carpooling.controllers;


import com.carpooling.carpooling.exceptions.AuthorizationException;
import com.carpooling.carpooling.helpers.AuthenticationHelper;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    public static final String ERROR_MESSAGE = "You are not authorized to browse user information.";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<User> getAllUsers(@RequestHeader HttpHeaders headers){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (!user.isAdmin()){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,ERROR_MESSAGE);
            }
            return userService.getAll();
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }

    @GetMapping("/{id:\\d+}")
    public User getUserById(@RequestHeader HttpHeaders headers,@PathVariable long id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id,user);
            return userService.getUserById(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }


    @PostMapping
    public User createUser(@RequestBody User newUser){
        try {
            return userService.create(newUser);
            } catch (Exception e){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
            }
    }


    @PutMapping("/{id}")
    public User updateUser(@RequestHeader HttpHeaders headers, @PathVariable long id, @RequestBody User updatedUser){
        try {
            System.out.println(" Received Update Request for User ID: " + id);
            System.out.println(" Incoming User object: " + updatedUser);

            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id,user);
            return userService.update(id,updatedUser);
        }catch (Exception e){
            System.out.println("Update Failed: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@RequestHeader HttpHeaders headers, @PathVariable long id){
        try {
            User requestingUser = authenticationHelper.tryGetUser(headers);

            if (!requestingUser.isAdmin() && requestingUser.getId() != id){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Your not allowed to delete this User");
            }
            userService.delete(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }
//    @GetMapping("/search")
//    public List<User> searchUser(@RequestHeader HttpHeaders headers,
//                                 @RequestParam(required = false) String username,
//                                 @RequestParam(required = false) String email,
//                                 @RequestParam(required = false) String phone){
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            if (!user.isAdmin()){
//                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,ERROR_MESSAGE);
//            }
//            return userService.se
//        }
//    }



    private static void checkAccessPermissions(long targetUserId, User executingUser){
        if (!executingUser.isAdmin() && executingUser.getId() != targetUserId){
            throw new AuthorizationException(ERROR_MESSAGE);
        }

    }

}

