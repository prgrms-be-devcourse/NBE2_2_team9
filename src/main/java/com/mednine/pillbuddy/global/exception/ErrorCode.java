package com.mednine.pillbuddy.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    MEDICATION_NOT_FOUND(NOT_FOUND, "약 정보를 찾을 수 없습니다."),
    MEDICATION_NOT_MATCHED(BAD_REQUEST, "약 정보가 일치하지 않습니다."),
    MEDICATION_NOT_REMOVED(CONFLICT, "약 정보 삭제에 실패했습니다."),
    MEDICATION_NOT_REGISTERED(CONFLICT, "약 정보 등록에 실패했습니다."),
    MEDICATION_NOT_MODIFIED(CONFLICT, "약 정보 수정에 실패했습니다."),
    MEDICATION_NOT_VALID(BAD_REQUEST, "유효하지 않는 약 정보입니다."),

    CARETAKER_CAREGIVER_NOT_FOUND(CONFLICT, "부모 정보 등록에 실패했습니다"),
    CARETAKER_CAREGIVER_REMOVED(CONFLICT, "부모 정보 삭제에 실패했습니다"),

    CARETAKER_NOT_FOUND(NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
    CARETAKER_NOT_MATCHED(BAD_REQUEST, "사용자 정보가 일치하지 않습니다."),
    CARETAKER_NOT_REMOVED(CONFLICT, "사용자 정보 삭제에 실패했습니다."),
    CARETAKER_NOT_REGISTERED(CONFLICT, "사용자 정보 등록에 실패했습니다."),
    CARETAKER_NOT_MODIFIED(CONFLICT, "사용자 정보 수정에 실패했습니다."),
    CARETAKER_NOT_VALID(BAD_REQUEST, "유효하지 않은 사용자 정보입니다."),

    CAREGIVER_NOT_FOUND(NOT_FOUND, "보호자 정보를 찾을 수 없습니다."),
    CAREGIVER_NOT_MATCHED(BAD_REQUEST, "보호자 정보가 일치하지 않습니다."),
    CAREGIVER_NOT_REMOVED(CONFLICT, "보호자 정보 삭제에 실패했습니다."),
    CAREGIVER_NOT_REGISTERED(CONFLICT, "보호자 정보 등록에 실패했습니다."),
    CAREGIVER_NOT_MODIFIED(CONFLICT, "보호자 정보 수정에 실패했습니다."),
    CAREGIVER_NOT_VALID(BAD_REQUEST, "유효하지 않은 보호자 정보입니다."),

    CARETAKER_CAREGIVER_NOT_REGISTERED(CONFLICT, "이미 등록된 정보입니다"),
    CARETAKER_CAREGIVER_NOT_VALID(CONFLICT, "유효하지 않은 정보입니다");


    private final HttpStatus httpStatus;
    private final String message;
}
