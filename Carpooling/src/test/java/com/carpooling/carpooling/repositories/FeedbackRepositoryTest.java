package com.carpooling.carpooling.repositories;

import com.carpooling.carpooling.models.Feedback;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TravelRepository travelRepository;

    @Test
    public void testFindByGiverId() {
        User giver = new User();
        giver.setUsername("giver");
        giver.setPassword("Giver@123");
        giver.setFirstName("Giver");
        giver.setLastName("Test");
        giver.setEmail("giver@example.com");
        giver.setPhoneNumber("123123123");

        userRepository.save(giver);

        User receiver = new User();
        receiver.setUsername("receiver");
        receiver.setPassword("Receiver@123");
        receiver.setFirstName("Receiver");
        receiver.setLastName("Test");
        receiver.setEmail("receiver@example.com");
        receiver.setPhoneNumber("987987987");

        userRepository.save(receiver);

        Travel travel = new Travel();
        travel.setDriver(giver);
        travel.setStartPoint("A");
        travel.setEndPoint("B");
        travel.setDepartureTime(LocalDateTime.now());
        travel.setFreeSpots(3);

        travelRepository.save(travel);

        Feedback feedback = new Feedback();
        feedback.setGiver(giver);
        feedback.setReceiver(receiver);
        feedback.setTravel(travel);
        feedback.setRating(5);
        feedback.setComment("Excellent service!");

        feedbackRepository.save(feedback);

        List<Feedback> feedbacks = feedbackRepository.findByGiverId(giver.getId());
        assertFalse(feedbacks.isEmpty());
        assertEquals("Excellent service!", feedbacks.get(0).getComment());
    }

}
