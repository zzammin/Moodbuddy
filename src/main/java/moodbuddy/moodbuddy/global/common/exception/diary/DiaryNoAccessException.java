package moodbuddy.moodbuddy.global.common.exception.diary;

import lombok.Getter;
import moodbuddy.moodbuddy.global.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
@Getter
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