package moodbuddy.moodbuddy.global.common.exception.diary;

import moodbuddy.moodbuddy.global.common.exception.ErrorCode;

public class DiaryNoAccessException extends RuntimeException {
    private final ErrorCode errorCode;
    public DiaryNoAccessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}