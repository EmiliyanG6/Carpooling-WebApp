package com.carpooling.carpooling.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    @Test
    public void testUserGetterAndSetters(){
        User user = new User();
        user.setUsername("Emiliyan");
        user.setPassword("12345");
        user.setFirstName("Emily");
        user.setLastName("Ivan");
        user.setEmail("emily@gmail.com");
        user.setPhoneNumber("1234567890");

        assertEquals("Emiliyan", user.getUsername());
        assertEquals("12345", user.getPassword());
        assertEquals("Emily", user.getFirstName());
        assertEquals("Ivan", user.getLastName());
        assertEquals("emily@gmail.com", user.getEmail());
        assertEquals("1234567890", user.getPhoneNumber());

    }

}
