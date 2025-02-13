package com.carpooling.carpooling.repositories;

import com.carpooling.carpooling.enums.PassengerStatus;
import com.carpooling.carpooling.models.Passenger;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PassengerRepositoryTest {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByTravelId() {
        User driver = new User();
        driver.setUsername("driver");
        driver.setPassword("Driver@123");
        driver.setFirstName("Driver");
        driver.setLastName("Test");
        driver.setEmail("driver@example.com");
        driver.setPhoneNumber("123456789");

        userRepository.save(driver);

        Travel travel = new Travel();
        travel.setDriver(driver);
        travel.setStartPoint("A");
        travel.setEndPoint("B");
        travel.setDepartureTime(LocalDateTime.now());
        travel.setFreeSpots(3);

        travelRepository.save(travel);

        User passengerUser = new User();
        passengerUser.setUsername("passenger");
        passengerUser.setPassword("Pass@123");
        passengerUser.setFirstName("Passenger");
        passengerUser.setLastName("Test");
        passengerUser.setEmail("passenger@example.com");
        passengerUser.setPhoneNumber("987654321");

        userRepository.save(passengerUser);

        Passenger passenger = new Passenger();
        passenger.setTravel(travel);
        passenger.setUser(passengerUser);
        passenger.setStatus(PassengerStatus.APPROVED);

        passengerRepository.save(passenger);

        List<Passenger> passengers = passengerRepository.findByTravelId(travel.getId());
        assertFalse(passengers.isEmpty());
        assertEquals("Confirmed", passengers.get(0).getStatus());
    }

}
