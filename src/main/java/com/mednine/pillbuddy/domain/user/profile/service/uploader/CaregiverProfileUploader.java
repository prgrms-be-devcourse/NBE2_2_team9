package com.mednine.pillbuddy.domain.user.profile.service.uploader;

import com.mednine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.mednine.pillbuddy.domain.user.caregiver.repository.CaregiverRepository;
import com.mednine.pillbuddy.domain.user.profile.entity.Image;
import com.mednine.pillbuddy.domain.user.profile.repository.ImageRepository;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import com.mednine.pillbuddy.global.util.UploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Transactional
public class CaregiverProfileUploader implements ProfileUploader{

    private final CaregiverRepository caregiverRepository;
    private final ImageRepository imageRepository;
    private final UploadUtils uploadUtils;


    @Override
    public void upload(MultipartFile file, Long userId) {
        Caregiver caregiver = caregiverRepository.findById(userId)
                .orElseThrow(() -> new PillBuddyCustomException(ErrorCode.USER_NOT_FOUND));

        Image image = caregiver.getImage();
        String filename = uploadUtils.upload(file);

        if (image != null) {
            // 이미지가 원래 있었다면, 삭제 후 새로운 url 업데이트
            uploadUtils.deleteFile(image.getUrl());
            image.updateUrl(filename);
        } else {
            image = Image.builder()
                    .url(filename)
                    .build();
        }

        // 예외 발생 시 업로드한 파일 삭제
        try {
            Image save = imageRepository.save(image);
            caregiver.updateImage(save);
        } catch (Exception e) {
            uploadUtils.deleteFile(filename);
        }
    }
}
