package com.mednine.pillbuddy.domain.user.profile.service.uploader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.domain.user.caretaker.repository.CaretakerRepository;
import com.mednine.pillbuddy.domain.user.profile.entity.Image;
import com.mednine.pillbuddy.domain.user.profile.repository.ImageRepository;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import com.mednine.pillbuddy.global.util.UploadUtils;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

class CaretakerProfileUploaderTest {

    @InjectMocks
    private CaretakerProfileUploader caretakerProfileUploader;

    @Mock
    private CaretakerRepository caretakerRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UploadUtils uploadUtils;

    private Caretaker caretaker;

    private Image image;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        caretaker = Caretaker.builder()
                .id(1L)
                .image(null)
                .build();

        image = Image.builder()
                .id(1L)
                .url("uploaded/test.jpg")
                .build();
    }

    @Test
    @DisplayName("사용자의 프로필 이미지를 업로드 할 수 있다.")
    void upload() {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg",
                "test image content".getBytes());
        when(caretakerRepository.findById(1L)).thenReturn(Optional.of(caretaker));
        when(uploadUtils.upload(file)).thenReturn("uploaded/test.jpg");
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        // When
        caretakerProfileUploader.upload(file, 1L);

        // Then
        assertThat(caretaker.getImage()).isEqualTo(image);
        assertThat(caretaker.getImage().getUrl()).isEqualTo("uploaded/test.jpg");
        verify(imageRepository).save(any(Image.class));
    }

    @Test
    @DisplayName("사용자가 존재하지 않다면 PillBuddyException 이 발생한다.")
    void upload_with_exception() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg",
                "test image content".getBytes());
        when(caretakerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> caretakerProfileUploader.upload(file, 1L))
                .isInstanceOf(PillBuddyCustomException.class)
                .hasMessage("회원 정보를 찾을 수 없습니다.");
    }
}