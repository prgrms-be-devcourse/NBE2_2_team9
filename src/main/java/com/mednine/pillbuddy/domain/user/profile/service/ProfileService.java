package com.mednine.pillbuddy.domain.user.profile.service;

import com.mednine.pillbuddy.domain.user.dto.UserType;
import com.mednine.pillbuddy.domain.user.profile.service.uploader.CaregiverProfileUploader;
import com.mednine.pillbuddy.domain.user.profile.service.uploader.CaretakerProfileUploader;
import com.mednine.pillbuddy.domain.user.profile.service.uploader.ProfileUploader;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final CaregiverProfileUploader caregiverProfileUploader;
    private final CaretakerProfileUploader caretakerProfileUploader;

    private final Map<UserType, ProfileUploader> uploaderMap = new HashMap<>();

    @PostConstruct
    public void initUploaderMap() {
        uploaderMap.put(UserType.CAREGIVER, caregiverProfileUploader);
        uploaderMap.put(UserType.CARETAKER, caretakerProfileUploader);
    }

    public void uploadProfile(MultipartFile file, Long userId, UserType userType) {
        ProfileUploader uploader = uploaderMap.get(userType);

        if (uploader == null) {
            throw new IllegalArgumentException("잘못된 사용자 형식입니다.");
        }
        uploader.upload(file, userId);
    }
}
