package com.medinine.pillbuddy.domain.user.oauth.service;

import com.medinine.pillbuddy.domain.user.entity.UserType;

public interface OAuthClient {

    String getConnectionUrl(UserType userType);
}
