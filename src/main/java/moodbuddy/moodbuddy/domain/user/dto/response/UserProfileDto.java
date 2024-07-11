package moodbuddy.moodbuddy.domain.user.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    LocalDateTime alarmTime;
    Boolean gender;
    LocalDateTime birthday;
}


