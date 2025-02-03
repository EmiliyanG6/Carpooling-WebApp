package com.carpooling.carpooling.controllers;


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
            return travelService.updateTravel(id,travel);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update travel", e);
        }
    }
}
