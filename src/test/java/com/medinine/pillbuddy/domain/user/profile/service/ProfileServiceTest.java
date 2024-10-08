package com.medinine.pillbuddy.domain.user.profile.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.medinine.pillbuddy.domain.user.dto.UserType;
import com.medinine.pillbuddy.domain.user.profile.dto.ProfileUploadDto;
import com.medinine.pillbuddy.global.exception.ErrorCode;
import com.medinine.pillbuddy.global.exception.PillBuddyCustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
class ProfileServiceTest {

    @Autowired
    private ProfileService profileService;

    @Test
    @DisplayName("잘못된 사용자 유형일 경우 예외가 발생한다.")
    void uploadProfile_invalid_userType() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
        UserType userType = null;

        Long userId = 1L;
        ProfileUploadDto profileUploadDto = new ProfileUploadDto(file, userType);

        assertThatThrownBy(() -> profileService.uploadProfile(userId, profileUploadDto))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage(ErrorCode.USER_INVALID_TYPE.getMessage());
    }

    @Test
    @DisplayName("비어있는 파일인 경우 예외가 발생한다.")
    void uploadProfile_file_is_null() {
        MockMultipartFile file = null;
        UserType userType = UserType.CAREGIVER;

        Long userId = 1L;
        ProfileUploadDto profileUploadDto = new ProfileUploadDto(file, userType);

        assertThatThrownBy(() -> profileService.uploadProfile(userId, profileUploadDto))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage(ErrorCode.PROFILE_INVALID_FILE.getMessage());
    }

    @Test
    @DisplayName("파일의 ContentType 이 image 로 시작하지 않으면 예외가 발생한다.")
    void uploadProfile_with_invalid_content_type() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "audio/mpeg", "test image content".getBytes());
        UserType userType = UserType.CAREGIVER;

        Long userId = 1L;
        ProfileUploadDto profileUploadDto = new ProfileUploadDto(file, userType);

        assertThatThrownBy(() -> profileService.uploadProfile(userId, profileUploadDto))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage(ErrorCode.PROFILE_INVALID_FILE_TYPE.getMessage());
    }

    @Test
    @DisplayName("파일의 이름이 비어 있는 경우 예외가 발생한다.")
    void uploadProfile_with_blank_file_name() {
        MockMultipartFile file = new MockMultipartFile("file", " ", "image/jpeg", "test image content".getBytes());
        UserType userType = UserType.CAREGIVER;

        Long userId = 1L;
        ProfileUploadDto profileUploadDto = new ProfileUploadDto(file, userType);

        assertThatThrownBy(() -> profileService.uploadProfile(userId, profileUploadDto))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage(ErrorCode.PROFILE_BLANK_FILE_NAME.getMessage());
    }
}
