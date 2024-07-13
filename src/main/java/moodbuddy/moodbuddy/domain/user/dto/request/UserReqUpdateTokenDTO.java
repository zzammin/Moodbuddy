package moodbuddy.moodbuddy.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserReqUpdateTokenDTO {
    @Schema(description = "유저의 fcmToken")
    String fcmToken;
}
