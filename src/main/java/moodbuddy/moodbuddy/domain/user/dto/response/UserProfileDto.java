package moodbuddy.moodbuddy.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserProfileDto {
    String url;
    String profileComment;
    String nickname;
    Boolean alarm;
    String alarmTime;
    String phoneNumber;
    Boolean gender;
    String birthday;
}


