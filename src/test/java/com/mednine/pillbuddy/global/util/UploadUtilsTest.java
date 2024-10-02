package com.mednine.pillbuddy.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

public class UploadUtilsTest {

    private UploadUtils uploadUtils;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        uploadUtils = new UploadUtils();
        ReflectionTestUtils.setField(uploadUtils, "uploadPath", tempDir.toString());
        ReflectionTestUtils.invokeMethod(uploadUtils, "init");
    }

    @Test
    @DisplayName("파일이 정상적으로 저장되는지 확인한다.")
    void upload() {
        // Given
        MockMultipartFile mockFile = new MockMultipartFile("file", "testFile.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});

        // When
        String savedFileName = uploadUtils.upload(mockFile);

        // Then
        File uploadedFile = new File(tempDir.toFile(), savedFileName);
        assertThat(uploadedFile.exists()).isTrue();
        assertThat(uploadedFile.getName()).contains("testFile");
        assertThat(savedFileName).contains("_");
    }

    @Test
    @DisplayName("파일 이름에 특수 문자나 공백이 포함되어 있는 경우, 대체되어 저장되는지 확인한다.")
    void upload_invalid_file_name() {
        // Given
        MockMultipartFile mockFile = new MockMultipartFile("file", "test    @file!.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});

        // When
        String savedFileName = uploadUtils.upload(mockFile);

        // Then
        assertThat(savedFileName).doesNotContain("@");
        assertThat(savedFileName).doesNotContain("!");
        assertThat(savedFileName).doesNotContain(" ");
        assertThat(savedFileName).contains("_");
    }

    @Test
    @DisplayName("파일이 정상적으로 삭제되는지 확인한다.")
    void deleteFile() {
        // Given
        MockMultipartFile mockFile = new MockMultipartFile("file", "testFile.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});
        String savedFileName = uploadUtils.upload(mockFile);
        File uploadedFile = new File(tempDir.toFile(), savedFileName);

        // When
        uploadUtils.deleteFile(savedFileName);

        // Then
        assertThat(uploadedFile.exists()).isFalse();
    }
}