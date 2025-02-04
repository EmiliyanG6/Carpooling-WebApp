package com.carpooling.carpooling.services;

import com.carpooling.carpooling.exceptions.AuthorizationException;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.repositories.TravelRepository;
import com.carpooling.carpooling.services.interfaces.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TravelServiceImpl implements TravelService {

    private final TravelRepository travelRepository;

    @Autowired
    public TravelServiceImpl(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    @Override
    public Travel createTravel(User user, Travel travel) {
        if (travelRepository.existsByDriverAndStartPointAndEndPointAndDepartureTime(
                travel.getDriver(),travel.getStartPoint(),travel.getEndPoint(),travel.getDepartureTime())){
            throw new IllegalArgumentException("You already have a similar travel scheduled");
        }
        travel.setDriver(user);
        return travelRepository.save(travel);
    }

    @Override
    public Travel updateTravel(Long id, Travel updatedTravel) {
        Travel existingTravel = getTravelById(id);

        if (updatedTravel.getStartPoint() != null) {
            existingTravel.setStartPoint(updatedTravel.getStartPoint());
        }
        if (updatedTravel.getEndPoint() != null) {
            existingTravel.setEndPoint(updatedTravel.getEndPoint());
        }
        if (updatedTravel.getDepartureTime() != null) {
            existingTravel.setDepartureTime(updatedTravel.getDepartureTime());
        }
        if (updatedTravel.getFreeSpots() != 0) {
            existingTravel.setFreeSpots(updatedTravel.getFreeSpots());
        }
        if (updatedTravel.getComment() != null) {
            existingTravel.setComment(updatedTravel.getComment());
        }

        return travelRepository.save(existingTravel);
    }

    @Override
    public void deleteTravel(Long id, User user) {
        checkModifyPermissions(id,user);
        travelRepository.deleteById(id);
    }

    @Override
    public Travel getTravelById(Long id) {
        return travelRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Travel not found"));
    }

    @Override
    public List<Travel> getAllTravels() {
        return travelRepository.findAll();
    }

    @Override
    public List<Travel> searchTravels(String startingPoint, String endingPoint, LocalDateTime departureTime) {
        return travelRepository.findByStartPointAndEndPointAndDepartureTime(
                startingPoint,endingPoint,departureTime);
    }

    private void checkModifyPermissions(long travelId, User user) {
        Travel travel = travelRepository.getById(travelId);
        if (!(user.isAdmin() || travel.getDriver().equals(user))) {
            throw new AuthorizationException("You don't have to delete this travel");
        }
    }
}
