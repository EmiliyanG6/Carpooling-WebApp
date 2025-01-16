package com.carpooling.carpooling.services.interfaces;

import com.carpooling.carpooling.models.Passenger;

import java.util.List;

public interface PassengerService {
    Passenger addPassangerToTravel(Long travelId, Passenger passenger);
    void removePassangerFromTravel(Long travelId, Long passengerId);
    List<Passenger> getPassengersByTravel(Long travelId);
    Passenger getPassengerById(Long id);
}
