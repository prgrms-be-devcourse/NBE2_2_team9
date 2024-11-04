package com.medinine.pillbuddy.domain.user.oauth.service;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthProfile;

public interface OAuthService {
    OAuthProfile getUserInfo(String code, UserType userType);
}
