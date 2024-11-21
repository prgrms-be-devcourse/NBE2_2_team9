package com.medinine.pillbuddy.domain.user.profile.service;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import com.medinine.pillbuddy.domain.user.profile.dto.ProfileUploadDto;
import com.medinine.pillbuddy.domain.user.profile.service.uploader.CaregiverProfileUploader;
import com.medinine.pillbuddy.domain.user.profile.service.uploader.CaretakerProfileUploader;
import com.medinine.pillbuddy.domain.user.profile.service.uploader.ProfileUploader;
import com.medinine.pillbuddy.global.exception.ErrorCode;
import com.medinine.pillbuddy.global.exception.PillBuddyCustomException;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private static final String FILE_TYPE_PREFIX = "image";

    private final CaregiverProfileUploader caregiverProfileUploader;
    private final CaretakerProfileUploader caretakerProfileUploader;

    private final Map<UserType, ProfileUploader> uploaderMap = new HashMap<>();

    @PostConstruct
    private void initUploaderMap() {
        uploaderMap.put(UserType.CAREGIVER, caregiverProfileUploader);
        uploaderMap.put(UserType.CARETAKER, caretakerProfileUploader);
    }

    public void uploadProfile(Long userId, ProfileUploadDto profileUploadDto) {
        MultipartFile file = profileUploadDto.getFile();
        UserType userType = profileUploadDto.getUserType();

        validateFile(file);
        ProfileUploader uploader = uploaderMap.get(userType);

        if (uploader == null) {
            throw new PillBuddyCustomException(ErrorCode.USER_INVALID_TYPE);
        }
        uploader.upload(file, userId);
    }

    private void validateFile(MultipartFile file) {
        if (file == null) {
            throw new PillBuddyCustomException(ErrorCode.PROFILE_INVALID_FILE);
        }

        if (!file.getContentType().startsWith(FILE_TYPE_PREFIX)) {
            throw new PillBuddyCustomException(ErrorCode.PROFILE_INVALID_FILE_TYPE);
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            throw new PillBuddyCustomException(ErrorCode.PROFILE_BLANK_FILE_NAME);
        }
    }
}
