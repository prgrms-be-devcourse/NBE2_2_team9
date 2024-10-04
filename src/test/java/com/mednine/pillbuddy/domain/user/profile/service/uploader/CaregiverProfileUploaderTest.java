package com.mednine.pillbuddy.domain.user.profile.service.uploader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.mednine.pillbuddy.domain.user.entity.Role;
import com.mednine.pillbuddy.domain.user.profile.entity.Image;
import com.mednine.pillbuddy.domain.user.profile.repository.ImageRepository;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import com.mednine.pillbuddy.global.util.UploadUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
public class CaregiverProfileUploaderTest {

    @Autowired
    private CaregiverProfileUploader caregiverProfileUploader;

    @Autowired
    private CaregiverRepository caregiverRepository;

    @Autowired
    private ImageRepository imageRepository;

    private String uploadedImageUrl;

    @AfterEach
    void cleanUp() {
        // 테스트가 완료되면 업로드된 이미지 삭제
        if (uploadedImageUrl != null) {
            UploadUtils.deleteFile(uploadedImageUrl);
        }
    }

    @Test
    @DisplayName("보호자의 프로필 이미지를 업로드 할 수 있다.")
    void test() {
        // given
        Caregiver caregiver = createCaregiver();
        Long caregiverId = caregiver.getId();

        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        // when
        caregiverProfileUploader.upload(file, caregiverId);
        Caregiver uploadedProfileUser = caregiverRepository.findById(caregiverId).get();
        Image savedImage = imageRepository.findById(uploadedProfileUser.getImage().getId()).get();
        Image uploadedImage = uploadedProfileUser.getImage();

        uploadedImageUrl = savedImage.getUrl(); // 삭제를 위해 추가

        // then
        assertThat(savedImage).isEqualTo(uploadedImage);

        assertThat(savedImage).isNotNull();
        assertThat(savedImage.getUrl()).endsWith("test.jpg");

        assertThat(uploadedImage).isNotNull();
        assertThat(uploadedImage.getUrl()).endsWith("test.jpg");
    }

    @Test
    @DisplayName("보호자가 존재하지 않다면 PillBuddyException 이 발생한다.")
    void upload_with_exception() {
        Long userId = 99999999L;
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        assertThatThrownBy(() -> caregiverProfileUploader.upload(file, userId))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage("회원 정보를 찾을 수 없습니다.");
    }

    private Caregiver createCaregiver() {
        Caregiver caregiver = Caregiver.builder()
                .username("test-caregiver")
                .loginId("test-loginId")
                .password("test-password")
                .email("test-email")
                .phoneNumber("test-phoneNumber")
                .role(Role.USER)
                .build();

        return caregiverRepository.save(caregiver);
    }
}
