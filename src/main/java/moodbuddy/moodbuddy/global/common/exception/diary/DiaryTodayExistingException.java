package moodbuddy.moodbuddy.global.common.exception.diary;

import moodbuddy.moodbuddy.global.common.exception.ErrorCode;

public class DiaryTodayExistingException extends RuntimeException {
    private final ErrorCode errorCode;
    public DiaryTodayExistingException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}