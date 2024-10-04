package com.mednine.pillbuddy.domain.user.dto;

import com.mednine.pillbuddy.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserDto {

    private String userType;
    private String username;
    private String loginId;
    private String email;
    private String phoneNumber;
    private String imageUrl;

    public UserDto(User user) {
        this.userType = user.getClass().getSimpleName().toLowerCase();
        this.username = user.getUsername();
        this.loginId = user.getLoginId();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();

        if (user.getImage() == null) {
            this.imageUrl = "default.jpg";
        } else {
            this.imageUrl = user.getImage().getUrl();
        }
    }
}
