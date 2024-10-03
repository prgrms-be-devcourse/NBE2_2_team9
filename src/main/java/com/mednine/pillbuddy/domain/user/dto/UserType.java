package com.mednine.pillbuddy.domain.user.dto;

import com.mednine.pillbuddy.domain.user.entity.User;

public enum UserType {
    CAREGIVER, CARETAKER;

    public static UserType from(User user) {
        String userType = user.getClass().getSimpleName().toUpperCase();
        return UserType.valueOf(userType);
    }
}
