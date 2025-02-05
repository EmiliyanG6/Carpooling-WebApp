package com.carpooling.carpooling.repositories;

import com.carpooling.carpooling.models.Passenger;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    List<Passenger> findByTravelId(Long travelId);
    List<Passenger> findByUserId(Long userId);
    List<Passenger> findByTravel(Travel travel);

    Optional<Passenger> findByUserAndTravel(User user, Travel travel);

    Optional<Passenger> findByUserIdAndTravelId(long userId, long id);
}
