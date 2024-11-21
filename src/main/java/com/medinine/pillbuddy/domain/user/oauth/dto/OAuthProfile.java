package com.medinine.pillbuddy.domain.user.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OAuthProfile {

    private String id;

    private String nickname;

    private String email;

    private String phoneNumber;
}
