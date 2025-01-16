package com.carpooling.carpooling.services.interfaces;

import com.carpooling.carpooling.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll();
    User getUserById(long id);
    User get(String username);
    User getUserByEmail(String email);
    void create(User user);
    void update(User updatedUser);
    void delete(int id);
}
