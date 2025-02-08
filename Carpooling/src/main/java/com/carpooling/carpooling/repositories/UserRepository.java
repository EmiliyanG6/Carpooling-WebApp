package com.carpooling.carpooling.repositories;

import com.carpooling.carpooling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User getUserById(long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
            "(:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
            "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:phone IS NULL OR u.phoneNumber LIKE CONCAT('%', :phone, '%'))")
    List<User> searchUsers(@Param("username") String username,
                           @Param("email") String email,
                           @Param("phone") String phone);

}
