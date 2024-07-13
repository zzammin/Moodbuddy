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
    Boolean gender;
    String birthday;
    @Schema(description = "사용자가 알람 설정을 할 때 (alarm을 ON으로 할 때) 보낼 푸시 알림을 위한 FCM Token")
    String fcmToken;
}


