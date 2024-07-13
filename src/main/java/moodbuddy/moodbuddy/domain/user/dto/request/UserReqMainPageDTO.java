package moodbuddy.moodbuddy.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReqMainPageDTO {
    @Schema(description = "사용자의 FCM Token")
    private String fcmToken;
}
