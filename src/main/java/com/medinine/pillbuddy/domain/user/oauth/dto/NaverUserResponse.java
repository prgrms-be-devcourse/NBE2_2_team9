package com.medinine.pillbuddy.domain.user.oauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverUserResponse {

    @JsonProperty("response")
    private NaverUserDetail naverUserDetail;

    @Getter
    @ToString
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NaverUserDetail {
        @JsonProperty("id")
        private String id;

        @JsonProperty("nickname")
        private String nickname;

        @JsonProperty("email")
        private String email;

        @JsonProperty("mobile")
        private String phoneNumber;
    }
}
