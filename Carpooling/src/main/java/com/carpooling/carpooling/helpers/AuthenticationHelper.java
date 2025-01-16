package com.carpooling.carpooling.helpers;


import com.carpooling.carpooling.exceptions.AuthorizationException;
import com.carpooling.carpooling.models.User;
import com.carpooling.carpooling.services.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    private static final String AUTHORIZATION_HEADER_NAME="Authorization";
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication";

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers){
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
        String email = getEmail(userInfo);
        String password = getPassword(userInfo);
        return verifyAutnentication(email, password);
    }
    public User tryGetCurrentUser(HttpSession session){
        String currentEmail = (String) session.getAttribute("currentUser");

        if (currentEmail == null){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userService.getUserByEmail(currentEmail);
    }

    public User verifyAutnentication(String email, String password){
        try {
            User user = userService.getUserByEmail(email);
            if (!user.getPassword().equals(password)){
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        }catch (EntityNotFoundException e){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }
    private String getEmail(String userInfo){
        int firstSpace = userInfo.indexOf(' ');
        if (firstSpace == -1){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userInfo.substring(0, firstSpace);
    }

    private String getPassword(String userInfo){
        int firstSpace = userInfo.indexOf(' ');
        if (firstSpace == -1){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userInfo.substring(firstSpace+1);
    }

}
