package moodbuddy.moodbuddy.domain.user.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserProfileUpdateDto {
    String url;
    String profileComment;
    String nickname;
    Boolean alarm;
    LocalDateTime alarmTime;
    Boolean gender;
    LocalDateTime birthday;
}
