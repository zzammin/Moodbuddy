package moodbuddy.moodbuddy.global.common.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class MemberIdNotFoundException extends RuntimeException {
    private String message;

    public MemberIdNotFoundException(final Long memberId) {
        super(memberId.toString());
        this.message = memberId.toString();
    }
}