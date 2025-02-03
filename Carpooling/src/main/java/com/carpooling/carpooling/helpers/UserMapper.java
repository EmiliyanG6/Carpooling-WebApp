package com.carpooling.carpooling.helpers;

import com.carpooling.carpooling.dto.RegisterDto;
import com.carpooling.carpooling.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User fromDto(RegisterDto registerDto){
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setEmail(registerDto.getEmail());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        return user;
    }


}
