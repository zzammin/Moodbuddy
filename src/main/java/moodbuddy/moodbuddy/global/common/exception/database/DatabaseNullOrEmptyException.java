package moodbuddy.moodbuddy.global.common.exception.database;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
public class DatabaseNullOrEmptyException extends RuntimeException{
    private String message;

    public DatabaseNullOrEmptyException(String message) {
        super(message);

        this.message = message;
    }
}

