package moodbuddy.moodbuddy.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class UserProfileUpdateDto {
    String profileComment;
    Boolean alarm;
    @Schema(description = "알림, HH:mm 형식")
    String alarmTime;
    Boolean gender;
    @Schema(description = "생일, YYYY-mm-dd 형식")
    String birthday;
    @Schema(description = "유저의 fcmToken")
    String fcmToken;
}
