package com.carpooling.carpooling.services;

import com.carpooling.carpooling.enums.PassengerStatus;
import com.carpooling.carpooling.exceptions.AuthorizationException;
import com.carpooling.carpooling.models.Passenger;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.repositories.PassengerRepository;
import com.carpooling.carpooling.repositories.TravelRepository;
import com.carpooling.carpooling.services.interfaces.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TravelServiceImpl implements TravelService {

    private final TravelRepository travelRepository;
    private final PassengerRepository passengerRepository;

    @Autowired
    public TravelServiceImpl(TravelRepository travelRepository, PassengerRepository passengerRepository) {
        this.travelRepository = travelRepository;
        this.passengerRepository = passengerRepository;
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
    public List<Travel> getAllTravels(int page, int size, String sortBy, String filterBy, String filterValue) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy != null ? sortBy : "departureTime");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Travel> travelsPage;

        if (filterBy != null && filterValue != null) {
            switch (filterBy.toLowerCase()) {
                case "startingpoint":
                    travelsPage = travelRepository.findByStartPoint(filterValue, pageable);
                    break;
                case "endingpoint":
                    travelsPage = travelRepository.findByEndPoint(filterValue, pageable);
                    break;
                default:
                    travelsPage = travelRepository.findAll(pageable);
            }
        } else {
            travelsPage = travelRepository.findAll(pageable);
        }


        return travelsPage.getContent();
    }

    @Override
    public List<Travel> searchTravels(String startingPoint, String endingPoint, LocalDateTime departureTime) {
        return travelRepository.findByStartPointAndEndPointAndDepartureTime(
                startingPoint,endingPoint,departureTime);
    }

    @Override
    public void applyForTravel(long id, User user) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Travel not found"));

        if (travel.getDriver().getId() == user.getId()) {
            throw new IllegalArgumentException("The driver cannot apply as a passenger");
        }

        Optional<Passenger> existingPassenger = passengerRepository.findByUserAndTravel(user,travel);
        if (existingPassenger.isPresent()) {
            throw new IllegalArgumentException("You already applied for this travel");
        }

        Passenger passenger = new Passenger();
        passenger.setUser(user);
        passenger.setTravel(travel);

        passengerRepository.save(passenger);
    }

    @Override
    public void approvePassenger(long travelId, long userId, User approvingUser) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(()-> new IllegalArgumentException("Travel not found"));

        if (!approvingUser.isAdmin() && travel.getDriver().getId() != approvingUser.getId()) {
            throw new IllegalArgumentException("You are not authorized to approve passengers.");
        }

        Passenger passenger = passengerRepository.findByUserIdAndTravelId(userId, travelId)
                .orElseThrow(()-> new IllegalArgumentException("Passenger not found"));

        if (travel.getFreeSpots() <= 0){
            throw new IllegalArgumentException("You are not authorized to approve passengers.");
        }
        passenger.setStatus(PassengerStatus.APPROVED);
        travel.setFreeSpots(travel.getFreeSpots() - 1);

        passengerRepository.save(passenger);
        travelRepository.save(travel);

    }

    @Override
    public void rejectPassenger(long travelId, long userId, User aprovingUser) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(()-> new IllegalArgumentException("Travel not found"));

        if (!aprovingUser.isAdmin() && travel.getDriver().getId() != aprovingUser.getId()) {
            throw new IllegalArgumentException("You are not authorized to reject passengers.");
        }
        Passenger passenger = passengerRepository.findByUserIdAndTravelId(userId,travelId)
                .orElseThrow(()-> new IllegalArgumentException("Passenger not found"));

        passenger.setStatus(PassengerStatus.REJECTED);
        passengerRepository.delete(passenger);
    }

    @Override
    public void canceTravel(long travelId, User existingUser) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(()-> new IllegalArgumentException("Travel not found"));

        if (!existingUser.isAdmin() && travel.getDriver().getId() != existingUser.getId()) {
            throw new IllegalArgumentException("You are not authorized to cancel travel.");
        }
        for (Passenger passenger : travel.getPassengers()) {
            passenger.setStatus(PassengerStatus.CANCELED);
        }

        travelRepository.delete(travel);

    }


    private void checkModifyPermissions(long travelId, User user) {
        Travel travel = travelRepository.getById(travelId);
        if (!(user.isAdmin() || travel.getDriver().equals(user))) {
            throw new AuthorizationException("You don't have to delete this travel");
        }
    }
}
