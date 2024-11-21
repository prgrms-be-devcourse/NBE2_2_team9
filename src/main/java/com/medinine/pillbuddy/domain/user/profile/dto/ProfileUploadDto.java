package com.medinine.pillbuddy.domain.user.profile.dto;

import com.medinine.pillbuddy.domain.user.entity.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ProfileUploadDto {

    @NotNull
    private MultipartFile file;

    @NotNull
    private UserType userType;
}
