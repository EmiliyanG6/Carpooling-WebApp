package com.carpooling.carpooling.services.interfaces;

import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;

import java.time.LocalDateTime;
import java.util.List;

public interface TravelService {
    Travel createTravel(User user, Travel travel);
    Travel updateTravel(Long id,Travel updatedTravel);
    void deleteTravel(Long id, User user);
    Travel getTravelById(Long id);
    List<Travel> getAllTravels(int page, int size, String sortBy, String filterBy, String filterValue);
    List<Travel> searchTravels(String startingPoint, String endingPoint, LocalDateTime departureTime);

    void applyForTravel(long id, User user);

    void approvePassenger(long id, long userId, User user);

    void rejectPassenger(long travelId, long userId, User user);

    void canceTravel(long id, User user);
}
