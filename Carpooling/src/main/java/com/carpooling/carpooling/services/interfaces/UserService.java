package com.carpooling.carpooling.services.interfaces;

import com.carpooling.carpooling.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    List<User> getAll();
    User getUserById(long id);
    User get(String username);
    User getUserByEmail(String email);
    User create(User user);
    User update(long id, User updatedUser);
    void delete(long id);

    void setUserBlockStatus(long id, boolean isBlocked);

    List<User> searchUsers(String username, String email, String phone);

    Page<User> getAllUsersPaginated(int page, int size, String sortBy, String direction);
}
