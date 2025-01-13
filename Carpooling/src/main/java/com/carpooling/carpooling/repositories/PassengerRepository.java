package com.carpooling.carpooling.repositories;

import com.carpooling.carpooling.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    List<Passenger> findByTravelId(Long travelId);
    List<Passenger> findByUserId(Long userId);
}
