package com.medinine.pillbuddy.domain.user.oauth.service;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.oauth.dto.OAuthProfile;
import com.medinine.pillbuddy.global.jwt.JwtToken;
import com.medinine.pillbuddy.global.jwt.JwtTokenProvider;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final Map<String, OAuthService> oAuthServiceMap;
    private final Map<String, OAuthClient> oAuthClientMap;

    private final UserReader userReader;
    private final JwtTokenProvider jwtTokenProvider;

    public String getConnectionUrl(UserType userType, String registrationId) {
        OAuthClient oAuthClient = oAuthClientMap.get(registrationId + "OAuthClient");
        return oAuthClient.getConnectionUrl(userType);
    }

    public JwtToken login(String code, UserType userType, String registrationId) {
        OAuthService oAuthService = oAuthServiceMap.get(registrationId + "OAuthService");
        OAuthProfile userInfo = oAuthService.getUserInfo(code, userType);

        if (!userReader.isNewUser(userInfo.getEmail(), userType)) {
            userReader.registerUser(userInfo, userType);
        }

        String loginID = userReader.getUserLoginID(userInfo.getEmail(), userType);
        return jwtTokenProvider.generateToken(loginID);
    }
}
