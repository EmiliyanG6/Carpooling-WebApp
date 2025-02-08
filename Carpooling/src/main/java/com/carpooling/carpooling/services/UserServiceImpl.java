package com.carpooling.carpooling.services;

import com.carpooling.carpooling.exceptions.EntityDuplicateException;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.repositories.UserRepository;
import com.carpooling.carpooling.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public User create(User user) {
        if (user == null){
            throw new IllegalArgumentException("User cannot be null");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new EntityDuplicateException("User","username",user.getUsername());
        }

        return userRepository.save(user);

    }

    @Override
    public User update(long id,User updatedUser) {
        System.out.println("Updating User ID: " + id);

        User existingUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional.ofNullable(updatedUser.getUsername()).ifPresent(existingUser::setUsername);
        Optional.ofNullable(updatedUser.getFirstName()).ifPresent(existingUser::setFirstName);
        Optional.ofNullable(updatedUser.getLastName()).ifPresent(existingUser::setLastName);
        Optional.ofNullable(updatedUser.getEmail()).ifPresent(existingUser::setEmail);
        Optional.ofNullable(updatedUser.getPhoneNumber()).ifPresent(existingUser::setPhoneNumber);

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()){
            existingUser.setPassword(updatedUser.getPassword());
        }else {
            System.out.println("Password is empty");
        }
        return userRepository.save(existingUser);
    }

    @Override
    public void delete(long id) {
        User user = userRepository.getUserById(id);

        if (user == null){
            throw new IllegalArgumentException("User not found");
        }
        userRepository.delete(user);
    }

    @Override
    public void setUserBlockStatus(long id, boolean isBlocked){
        User user = getUserById(id);
        user.setBlocked(isBlocked);
        userRepository.save(user);
    }

    @Override
    public List<User> searchUsers(String username,String email, String phone){
        return userRepository.searchUsers(username,email,phone);
    }



}
