package moodbuddy.moodbuddy.global.common.exception.quddyTI;

import lombok.Getter;
import moodbuddy.moodbuddy.global.common.exception.ErrorCode;
public class QuddyTINotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
    public QuddyTINotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}