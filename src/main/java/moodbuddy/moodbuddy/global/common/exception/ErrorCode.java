package moodbuddy.moodbuddy.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    OK(200, "COMMON_SUCCESS-200", "Successful"),
    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    INTERNAL_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),
    EMAIL_DUPLICATION(400,"MEMBER-ERR-400","EMAIL DUPLICATED"),
    NO_MATCHING_CONTENTS(204, "NO-CONTENT_ERR-204", "NO CONTENT"),
    TODAY_EXISTING_DIARY(409, "DIARY-ERR-409", "오늘 이미 일기를 작성했습니다.");

    private int status;
    private String errorCode;
    private String message;
}

