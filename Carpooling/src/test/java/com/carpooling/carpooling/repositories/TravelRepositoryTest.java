package com.carpooling.carpooling.repositories;

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
public class TravelRepositoryTest {

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByDriverId() {
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

        List<Travel> travels = travelRepository.findByDriverId(driver.getId());
        assertFalse(travels.isEmpty());
        assertEquals("A", travels.get(0).getStartPoint());
    }

}
