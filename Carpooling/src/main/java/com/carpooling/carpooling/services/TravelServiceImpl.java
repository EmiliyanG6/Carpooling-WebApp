package com.carpooling.carpooling.services;

import com.carpooling.carpooling.models.Travel;
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
    public Travel createTravel(Travel travel) {
        if (travelRepository.existsByDriverAndStartPointAndEndPointAndDepartureTime(
                travel.getDriver(),travel.getStartPoint(),travel.getEndPoint(),travel.getDepartureTime())){
            throw new IllegalArgumentException("You already have a similar travel scheduled");
        }
        return travelRepository.save(travel);
    }

    @Override
    public Travel updateTravel(Long id, Travel updatedTravel) {
        Travel existingTravel = travelRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Travel not found"));

        if (travelRepository.existsByDriverAndStartPointAndEndPointAndDepartureTime(
                updatedTravel.getDriver(),updatedTravel.getStartPoint(),updatedTravel.getEndPoint()
                ,updatedTravel.getDepartureTime())){
            throw new IllegalArgumentException("You already have a similar travel scheduled");
        }
        existingTravel.setStartPoint(updatedTravel.getStartPoint());
        existingTravel.setEndPoint(updatedTravel.getEndPoint());
        existingTravel.setDepartureTime(updatedTravel.getDepartureTime());
        existingTravel.setFreeSpots(updatedTravel.getFreeSpots());
        existingTravel.setComment(updatedTravel.getComment());
        return travelRepository.save(existingTravel);
    }

    @Override
    public void deleteTravel(Long id) {
        if (!travelRepository.existsById(id)) {
            throw new IllegalArgumentException("Travel not found");
        }
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
}
