package com.carpooling.carpooling.controllers.rest;

import com.carpooling.carpooling.exceptions.AuthorizationException;
import com.carpooling.carpooling.helpers.AuthenticationHelper;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private static final String ERROR_MESSAGE = "You are not authorized to perform this action.";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/{id:\\d+}")
    public User getUserById(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, user);
            return userService.getUserById(id);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to retrieve user", e);
        }
    }

    @PostMapping
    public User createUser(@RequestBody User newUser) {
        try {
            return userService.create(newUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create user", e);
        }
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestHeader HttpHeaders headers, @PathVariable long id, @RequestBody User updatedUser) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, user);
            return userService.update(id, updatedUser);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update user", e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            User requestingUser = authenticationHelper.tryGetUser(headers);

            if (!requestingUser.isAdmin() && requestingUser.getId() != id) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this user.");
            }
            userService.delete(id);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to delete user", e);
        }
    }

    @PutMapping("/{id}/block")
    public void setUserBlockStatus(@RequestHeader HttpHeaders headers,
                                   @PathVariable long id,
                                   @RequestParam boolean block) {
        try {
            User admin = authenticationHelper.tryGetUser(headers);
            checkAdminAccess(admin);

            userService.setUserBlockStatus(id, block);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update user block status", e);
        }
    }

    @GetMapping("/search")
    public List<User> searchUsers(@RequestHeader HttpHeaders headers,
                                  @RequestParam(required = false) String username,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String phone) {
        try {
            User admin = authenticationHelper.tryGetUser(headers);
            checkAdminAccess(admin);

            return userService.searchUsers(username, email, phone);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to search users", e);
        }
    }

    @GetMapping
    public Page<User> getAllUsers(@RequestHeader HttpHeaders headers,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortBy,
                                  @RequestParam(defaultValue = "ASC") String direction) {
        try {
            User admin = authenticationHelper.tryGetUser(headers);
            checkAdminAccess(admin);

            return userService.getAllUsersPaginated(page, size, sortBy, direction);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to retrieve users", e);
        }
    }

    private static void checkAccessPermissions(long targetUserId, User executingUser) {
        if (!executingUser.isAdmin() && executingUser.getId() != targetUserId) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    private void checkAdminAccess(User user) {
        if (!user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to perform this action.");
        }
    }
}
