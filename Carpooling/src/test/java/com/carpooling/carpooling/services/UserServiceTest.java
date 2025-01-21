package com.carpooling.carpooling.services;

import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.repositories.UserRepository;
import com.carpooling.carpooling.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUserByUsername_Success(){
        String username = "testUser";
        User mockUser = new User();
        mockUser.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        User foundUser = userService.get(username);

        assertNotNull(foundUser);
        assertEquals(username,foundUser.getUsername());
        verify(userRepository,times(1)).findByUsername(username);
    }
    @Test
    void testFindUserByUsername_UserNotFound(){
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> userService.get(username));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository,times(1)).findByUsername(username);
    }
    @Test
    void testCreateUser_Success(){
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("Password1!");
        user.setEmail("test@example.com");
        
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        userService.create(user);

        verify(userRepository, times(1)).findByUsername("testUser");
        verify(userRepository, times(1)).save(user);
    }
    @Test
    void testCreateUser_ThrowsExceptionsOnNullUser(){
        User user = null;

        assertThrows(IllegalArgumentException.class, () -> userService.create(user));
        verify(userRepository, never()).save(any());
    }
    @Test
    void testUpdatedUser_Success() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("OldPassword!");
        existingUser.setFirstName("OldFirstName");
        existingUser.setLastName("OldLastName");
        existingUser.setEmail("old@example.com");
        existingUser.setPhoneNumber("1234567890");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("newUsername");
        updatedUser.setPassword("NewPassword!");
        updatedUser.setFirstName("NewFirstName");
        updatedUser.setLastName("NewLastName");
        updatedUser.setEmail("new@example.com");
        updatedUser.setPhoneNumber("0987654321");

        when(userRepository.getUserById(userId)).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        userService.update(updatedUser);

        verify(userRepository, times(1)).getUserById(userId);
        verify(userRepository, times(1)).save(existingUser);

        assertEquals("newUsername", existingUser.getUsername());
        assertEquals("NewPassword!", existingUser.getPassword());
        assertEquals("NewFirstName", existingUser.getFirstName());
        assertEquals("NewLastName", existingUser.getLastName());
        assertEquals("new@example.com", existingUser.getEmail());
        assertEquals("0987654321", existingUser.getPhoneNumber());
    }

    @Test
    void testDeleteUser_Success(){
        String username = "existingUser";
        User user = new User();
        user.setUsername(username);
        user.setId(1L);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        userService.delete(username);

        verify(userRepository,times(1)).findByUsername(username);
        verify(userRepository,times(1)).delete(user);
    }
    @Test
    void testGetAllUsers_Success(){
        User user1= new User();
        user1.setId(1L);
        user1.setUsername("testUser");

        User user2= new User();
        user2.setId(2L);
        user2.setUsername("testUser2");

        List<User> mockUsers = List.of(user1,user2);
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> users = userService.getAll();

        assertNotNull(users);
        assertEquals(2,users.size());
        assertEquals("testUser",users.get(0).getUsername());
        assertEquals("testUser2",users.get(1).getUsername());
        verify(userRepository,times(1)).findAll();
    }
    @Test
    void testGetUserById_Success(){
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");

        when(userRepository.getUserById(userId)).thenReturn(user);

        User fetchedUser = userService.getUserById(userId);

        assertNotNull(fetchedUser);
        assertEquals(userId,fetchedUser.getId());
        assertEquals("testUser",fetchedUser.getUsername());
        verify(userRepository,times(1)).getUserById(userId);
    }
}
