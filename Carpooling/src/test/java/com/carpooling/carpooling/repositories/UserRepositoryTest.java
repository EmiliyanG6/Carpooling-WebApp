package com.carpooling.carpooling.repositories;

import com.carpooling.carpooling.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindByUsername() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("Test@1234");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("1234567890");

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("testUser");
        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
    }
    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setUsername("testUser2");
        user.setPassword("Password@123");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("jane.smith@example.com");
        user.setPhoneNumber("9876543210");

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("jane.smith@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("jane.smith@example.com", foundUser.get().getEmail());
    }
}
