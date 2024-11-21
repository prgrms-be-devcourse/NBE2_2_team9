package com.medinine.pillbuddy.domain.user.oauth.service;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthProfile;

public interface OAuthClient {

    String getConnectionUrl(UserType userType);
    OAuthProfile getUserInfo(String code);
}
