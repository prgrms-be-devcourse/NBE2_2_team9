package com.medinine.pillbuddy.domain.user.entity;

import com.medinine.pillbuddy.global.exception.ErrorCode;
import com.medinine.pillbuddy.global.exception.PillBuddyCustomException;

public enum UserType {
    CAREGIVER, CARETAKER;

    public static UserType from(User user) {
        String userType = user.getClass().getSimpleName().toUpperCase();
        return UserType.valueOf(userType);
    }

    public static UserType from(String userType) {
        try {
            return UserType.valueOf(userType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new PillBuddyCustomException(ErrorCode.USER_INVALID_TYPE);
        }
    }
}
