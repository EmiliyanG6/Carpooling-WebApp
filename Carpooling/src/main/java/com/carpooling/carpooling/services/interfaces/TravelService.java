package com.carpooling.carpooling.services.interfaces;

import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;

import java.time.LocalDateTime;
import java.util.List;

public interface TravelService {
    Travel createTravel(User user, Travel travel);
    Travel updateTravel(Long id,Travel updatedTravel);
    void deleteTravel(Long id);
    Travel getTravelById(Long id);
    List<Travel> getAllTravels();
    List<Travel> searchTravels(String startingPoint, String endingPoint, LocalDateTime departureTime);
}
