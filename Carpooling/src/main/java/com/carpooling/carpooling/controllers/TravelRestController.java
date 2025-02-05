package com.carpooling.carpooling.controllers;


import com.carpooling.carpooling.exceptions.AuthorizationException;
import com.carpooling.carpooling.helpers.AuthenticationHelper;
import com.carpooling.carpooling.helpers.TravelMapper;
import com.carpooling.carpooling.models.Dtos.TravelDto;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.services.interfaces.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
public class TravelRestController {


    public static final String ERROR_MESSAGE = "You are not authorized to browse user information.";

    private final TravelService travelService;
    private final AuthenticationHelper authenticationHelper;
    private final TravelMapper travelMapper;

    @Autowired
    public TravelRestController(TravelService travelService, AuthenticationHelper authenticationHelper, TravelMapper travelMapper) {
        this.travelService = travelService;
        this.authenticationHelper = authenticationHelper;
        this.travelMapper = travelMapper;
    }

    @GetMapping("/{id}")
    public TravelDto getTravelById(@PathVariable long id) {
        try {
            Travel travel = travelService.getTravelById(id);
            return new TravelDto(travel);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public List<Travel> getAllTravels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size,
            @RequestParam(required = false)String sortBy,
            @RequestParam(required = false)String filterBy,
            @RequestParam(required = false)String filterValue) {
        return travelService.getAllTravels(page,size,sortBy,filterBy,filterValue);
    }



    @PostMapping
    public Travel createTravel(@RequestHeader HttpHeaders headers, @RequestBody TravelDto travelDto){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Travel travel = travelMapper.fromDto(travelDto);
            travel.setDriver(user);
            return travelService.createTravel(user,travel);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create travel", e);
        }
    }

    @PutMapping("/{id}")
    public Travel updateTravel(@RequestHeader HttpHeaders headers,
                               @PathVariable long id,
                               @RequestBody TravelDto travelDto){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Travel travel = travelMapper.fromDto(id,travelDto);
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

    @PostMapping("/{id}/apply")
    public void applyForTravel(@RequestHeader HttpHeaders headers, @PathVariable long id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            travelService.applyForTravel(id,user);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to apply for travel");
        }
    }
    @PutMapping("/{id}/approve/{userId}")
    public void approvePassenger(@RequestHeader HttpHeaders headers, @PathVariable long id,@PathVariable long userId){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            travelService.approvePassenger(id,userId,user);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to approve passenger");
        }
    }
}
