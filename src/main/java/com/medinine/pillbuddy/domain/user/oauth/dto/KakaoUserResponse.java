package com.medinine.pillbuddy.domain.user.oauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("properties")
    private HashMap<String, String> properties; // profile_image, nickname

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    @NoArgsConstructor
    @ToString
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class KakaoAccount {

        @JsonProperty("email")
        private String email;
    }
}
