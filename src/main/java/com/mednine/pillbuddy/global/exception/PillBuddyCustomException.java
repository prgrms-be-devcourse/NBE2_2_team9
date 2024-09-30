package com.mednine.pillbuddy.global.exception;

import lombok.Getter;

@Getter
public class PillBuddyCustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public PillBuddyCustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
