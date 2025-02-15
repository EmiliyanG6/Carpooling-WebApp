package com.carpooling.carpooling.helpers;

import com.carpooling.carpooling.models.Dtos.RegisterDto;
import com.carpooling.carpooling.models.Dtos.UserDto;
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
        user.setPhoneNumber(registerDto.getPhoneNumber());
        return user;
    }
    public UserDto toDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }


    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }
}
