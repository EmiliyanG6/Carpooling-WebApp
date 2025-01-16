package com.carpooling.carpooling.services;

import com.carpooling.carpooling.exceptions.EntityDuplicateException;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.repositories.UserRepository;
import com.carpooling.carpooling.services.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public User get(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public void create(User user) {
        boolean dublicate = true;

        try {
            userRepository.findByUsername(user.getUsername());
        }catch (EntityNotFoundException e){
            dublicate = false;
        }
        if (dublicate) {
            throw new EntityDuplicateException("User","username",user.getUsername());
        }

        userRepository.save(user);
    }

    @Override
    public void update(User updatedUser) {
        User existingUser = userRepository.getUserById(updatedUser.getId());

        if (existingUser == null){
            throw new IllegalArgumentException("User not found");
        }
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        userRepository.save(existingUser);

    }

    @Override
    public void delete(int id) {
        Long userId = (long) id;

        if (!userRepository.existsById(userId)){
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(userId);
    }
}
