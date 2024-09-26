package com.mednine.pillbuddy.global.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtToken {

    private String grantType;
    private String accessToken;
    private String refreshToken;
}
