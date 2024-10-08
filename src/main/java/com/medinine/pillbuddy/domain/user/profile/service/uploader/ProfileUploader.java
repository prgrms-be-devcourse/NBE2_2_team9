package com.medinine.pillbuddy.domain.user.profile.service.uploader;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileUploader {

    void upload(MultipartFile file, Long userId);
}
