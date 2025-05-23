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

        String username = getUsername(userInfo);
        String password = getPassword(userInfo);

        return verifyAutnentication(username, password);
    }
    public User tryGetCurrentUser(HttpSession session){
        String currentUsername = (String) session.getAttribute("currentUser");

        if (currentUsername == null){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userService.get(currentUsername);
    }

    public User verifyAutnentication(String username, String password){
        try {
            User user = userService.get(username);
            if (!user.getPassword().equals(password)){
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        }catch (EntityNotFoundException e){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }
    private String getUsername(String userInfo){
        int firstSpace = userInfo.indexOf(" ");
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
