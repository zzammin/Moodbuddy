package moodbuddy.moodbuddy.domain.user.dto.fcm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmReqDTO {
    @Schema(description = "클라이언트 디바이스에서 발급받은 Fcm Token")
    private String token;
    @Schema(description = "FCM 푸시 알림 메시지의 제목")
    private String title;
    @Schema(description = "FCM 푸시 알림 메시지의 내용")
    private String body;
}
