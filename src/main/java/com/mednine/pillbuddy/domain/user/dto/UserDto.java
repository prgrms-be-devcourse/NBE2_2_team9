package com.mednine.pillbuddy.domain.user.dto;

import com.mednine.pillbuddy.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserDto {

    private String userType;
    private String username;
    private String email;
    private String phoneNumber;

    public UserDto(User user) {
        this.userType = user.getClass().getSimpleName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
    }
}
