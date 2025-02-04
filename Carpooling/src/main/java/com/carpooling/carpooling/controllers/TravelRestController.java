package com.carpooling.carpooling.controllers;


import com.carpooling.carpooling.exceptions.AuthorizationException;
import com.carpooling.carpooling.helpers.AuthenticationHelper;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.services.interfaces.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/travels")
public class TravelRestController {


    public static final String ERROR_MESSAGE = "You are not authorized to browse user information.";

    private final TravelService travelService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TravelRestController(TravelService travelService, AuthenticationHelper authenticationHelper) {
        this.travelService = travelService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/{id}")
    public Travel getTravelById(@PathVariable long id) {
        try {
            return travelService.getTravelById(id);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Travel createTravel(@RequestHeader HttpHeaders headers, @RequestBody Travel travel){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            travel.setDriver(user);
            return travelService.createTravel(user,travel);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create travel", e);
        }
    }

    @PutMapping("/{id}")
    public Travel updateTravel(@RequestHeader HttpHeaders headers,
                               @PathVariable long id,
                               @RequestBody Travel travel){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id,user);
            return travelService.updateTravel(id,travel);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update travel", e);
        }
    }

    @DeleteMapping("/{travelId}")
    public void deleteTravel(@PathVariable long travelId, @RequestHeader HttpHeaders headers){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            travelService.deleteTravel(travelId,user);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"You are not authorized to delete travel");
        }
    }







    private static void checkAccessPermissions(long targetUserId, User executingUser){

        if (!executingUser.isAdmin() && executingUser.getId() != targetUserId){
            throw new AuthorizationException(ERROR_MESSAGE);
        }

    }
}
