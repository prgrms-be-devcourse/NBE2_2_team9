package com.mednine.pillbuddy.global.util;

import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class UploadUtils {

    private static final String FILE_NAME_REGEX = "[^a-zA-Z0-9.\\-_]";
    private static final String FILE_NAME_REPLACEMENT = "_";
    private static final String FILE_TYPE_PREFIX = "image";

    @Value("${file.path}")
    private String uploadPath;

    @PostConstruct
    private void init() {
        File tempDir = new File(uploadPath);

        if (!tempDir.exists()) {
            boolean created = tempDir.mkdir();
            if (!created) {
                throw new PillBuddyCustomException(ErrorCode.PROFILE_CREATE_DIRECTORY_FAIL);
            }
        }
        uploadPath = tempDir.getAbsolutePath();
    }

    public String upload(MultipartFile file) {
        if (file == null || file.getContentType() == null) {
            throw new PillBuddyCustomException(ErrorCode.PROFILE_INVALID_FILE);
        }

        if (!file.getContentType().startsWith(FILE_TYPE_PREFIX)) {
            throw new PillBuddyCustomException(ErrorCode.PROFILE_INVALID_FILE_TYPE);
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            throw new PillBuddyCustomException(ErrorCode.PROFILE_BLANK_FILE_NAME);
        }

        String uuid = UUID.randomUUID().toString();
        String saveFileName = uuid + FILE_NAME_REPLACEMENT + cleanFileName(file.getOriginalFilename());

        try {
            file.transferTo(new File(uploadPath, saveFileName));
        } catch (IOException e) {
            throw new PillBuddyCustomException(ErrorCode.PROFILE_NOT_SUPPORT_FILE_TYPE);
        }
        return saveFileName;
    }

    // 파일 이름에 특수 문자나 공백이 포함되어 있는 경우, 그 문자를 밑줄(_)로 대체
    private String cleanFileName(String fileName) {
        return fileName.replaceAll(FILE_NAME_REGEX, FILE_NAME_REPLACEMENT);
    }

    public void deleteFile(String fileName) {
        File file = new File(uploadPath, fileName);

        if (file.exists() && !file.delete()) {
            throw new PillBuddyCustomException(ErrorCode.PROFILE_DELETE_FILE_FAIL);
        }
    }
}
