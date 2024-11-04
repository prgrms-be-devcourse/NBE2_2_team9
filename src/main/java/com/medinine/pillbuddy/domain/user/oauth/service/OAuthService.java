package com.medinine.pillbuddy.domain.user.oauth.service;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.global.jwt.JwtToken;

public interface OAuthService {
    String getConnectionUrl(UserType userType);
    JwtToken login(String code, UserType userType);
}
