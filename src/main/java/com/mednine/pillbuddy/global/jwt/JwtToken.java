package com.mednine.pillbuddy.global.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class JwtToken {

    private String grantType;
    private String accessToken;
    private String refreshToken;
}
