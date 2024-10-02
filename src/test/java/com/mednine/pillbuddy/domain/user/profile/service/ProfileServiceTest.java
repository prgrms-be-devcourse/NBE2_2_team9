package com.mednine.pillbuddy.domain.user.profile.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.mednine.pillbuddy.domain.user.dto.UserType;
import com.mednine.pillbuddy.domain.user.profile.dto.ProfileUploadDto;
import com.mednine.pillbuddy.domain.user.profile.service.uploader.CaregiverProfileUploader;
import com.mednine.pillbuddy.domain.user.profile.service.uploader.CaretakerProfileUploader;
import com.mednine.pillbuddy.domain.user.profile.service.uploader.ProfileUploader;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

class ProfileServiceTest {

    @InjectMocks
    private ProfileService profileService;

    @Mock
    private CaregiverProfileUploader caregiverProfileUploader;

    @Mock
    private CaretakerProfileUploader caretakerProfileUploader;

    @Mock
    private Map<UserType, ProfileUploader> uploaderMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(uploaderMap.get(UserType.CAREGIVER)).thenReturn(caregiverProfileUploader);
        when(uploaderMap.get(UserType.CARETAKER)).thenReturn(caretakerProfileUploader);
    }

    @Test
    @DisplayName("잘못된 사용자 유형일 경우 예외가 발생한다.")
    void uploadProfile_invalid_userType() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg",
                "test image content".getBytes());
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
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "audio/mpeg",
                "test image content".getBytes());
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
