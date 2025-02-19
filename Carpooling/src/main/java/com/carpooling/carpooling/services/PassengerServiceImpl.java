package com.carpooling.carpooling.services;

import com.carpooling.carpooling.enums.PassengerStatus;
import com.carpooling.carpooling.models.Passenger;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.repositories.PassengerRepository;
import com.carpooling.carpooling.repositories.TravelRepository;
import com.carpooling.carpooling.services.interfaces.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final TravelRepository travelRepository;

    @Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository, TravelRepository travelRepository) {
        this.passengerRepository = passengerRepository;
        this.travelRepository = travelRepository;
    }

    @Override
    public Passenger addPassangerToTravel(Long travelId, Passenger passenger) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new IllegalArgumentException("Travel not found"));

        if (travel.getFreeSpots() <= 0) {
            throw new IllegalStateException("No free spots available for this travel");
        }

        passenger.setTravel(travel);
        passenger.setStatus(PassengerStatus.PENDING);
        return passengerRepository.save(passenger);
    }

    @Override
    public void removePassangerFromTravel(Long travelId, Long passengerId) {

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new IllegalArgumentException("Travel not found"));
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found"));

        passengerRepository.delete(passenger);
        travel.setFreeSpots(travel.getFreeSpots()+1);
        travelRepository.save(travel);

    }

    @Override
    public List<Passenger> getPassengersByTravel(Long travelId) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new IllegalArgumentException("Travel not found"));

        return passengerRepository.findByTravel(travel);
    }

    @Override
    public Passenger getPassengerById(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found"));
    }
}
